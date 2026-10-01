package net.hans.hugoboss.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class GiantEagleEntity extends TamableAnimal {

    private static final EntityDataAccessor<Boolean> DATA_SADDLED = 
            SynchedEntityData.defineId(GiantEagleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_FLYING = 
            SynchedEntityData.defineId(GiantEagleEntity.class, EntityDataSerializers.BOOLEAN);

    private int eatingTicks = 0;

    public GiantEagleEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        var scaleAttr = this.getAttribute(Attributes.SCALE);
        if (scaleAttr != null) {
            scaleAttr.setBaseValue(2.8D); // Stor örn lämplig för ridning
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SADDLED, false);
        builder.define(DATA_FLYING, true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingNavigation = new FlyingPathNavigation(this, level);
        flyingNavigation.setCanOpenDoors(false);
        flyingNavigation.setCanFloat(true);
        return flyingNavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new EagleSwoopAndAttackGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.2D, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new EagleRandomFlyGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        // Jaktmål: Attackerar vilda kaniner, hönor och vatten-djur (fiskar) om örnen INTE är tämjd
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Rabbit.class, 10, true, false, (e, level) -> !this.isTame()));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, 10, true, false, (e, level) -> !this.isTame()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, WaterAnimal.class, 10, true, false, (e, level) -> !this.isTame()));
    }

    public boolean isSaddled() {
        return this.entityData.get(DATA_SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(DATA_SADDLED, saddled);
    }

    public boolean isEagleFlying() {
        return this.entityData.get(DATA_FLYING);
    }

    public void setEagleFlying(boolean flying) {
        this.entityData.set(DATA_FLYING, flying);
        this.setNoGravity(flying);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.RABBIT) || stack.is(Items.CHICKEN) || 
               stack.is(Items.COD) || stack.is(Items.SALMON);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.isTame() && this.isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (!this.level().isClientSide()) {
                if (this.random.nextInt(3) == 0) { // 33% chans att tämjas
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte) 7); // Tame heart particles
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6); // Smoke particles
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && this.isOwnedBy(player)) {
            if (stack.is(Items.SADDLE) && !this.isSaddled()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.setSaddled(true);
                this.playSound(SoundEvents.HORSE_SADDLE.value(), 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            if (this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
                if (!this.level().isClientSide()) {
                    player.startRiding(this);
                }
                return InteractionResult.SUCCESS;
            }

            if (!stack.is(Items.SADDLE) && !this.isFood(stack)) {
                this.setOrderedToSit(!this.isOrderedToSit());
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.eatingTicks > 0) {
            this.eatingTicks--;
        }

        if (this.onGround() && this.isEagleFlying()) {
            this.setEagleFlying(false);
        } else if (!this.onGround() && !this.isEagleFlying() && !this.isInWater()) {
            this.setEagleFlying(true);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isAlive() && this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            this.setRot(player.getYRot(), player.getXRot() * 0.5F);
            this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();

            double forward = player.zza;
            double strafe = player.xxa;

            if (forward <= 0.0D) {
                forward *= 0.25D;
            }

            double verticalSpeed = 0.0D;
            if (this.jumping) {
                verticalSpeed = 0.35D;
                this.setEagleFlying(true);
            } else if (this.isEagleFlying()) {
                verticalSpeed = player.getXRot() * -0.01D;
            }

            if (this.isEagleFlying()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, verticalSpeed, 0));
                this.move(MoverType.SELF, this.getDeltaMovement());
                super.travel(new Vec3(strafe * 0.3D, travelVector.y, forward * 0.5D));
            } else {
                super.travel(new Vec3(strafe * 0.3D, travelVector.y, forward * 0.3D));
            }
            return;
        }

        super.travel(travelVector);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.GIANT_EAGLE.get().create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Saddled", this.isSaddled());
        output.putBoolean("Flying", this.isEagleFlying());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSaddled(input.getBooleanOr("Saddled", false));
        this.setEagleFlying(input.getBooleanOr("Flying", true));
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // Ignorera fallskada för örn
    }

    static class EagleSwoopAndAttackGoal extends Goal {
        private final GiantEagleEntity eagle;

        public EagleSwoopAndAttackGoal(GiantEagleEntity eagle) {
            this.eagle = eagle;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.eagle.getTarget();
            return target != null && target.isAlive() && !this.eagle.isVehicle();
        }

        @Override
        public void tick() {
            LivingEntity target = this.eagle.getTarget();
            if (target != null) {
                this.eagle.getLookControl().setLookAt(target, 30.0F, 30.0F);
                Vec3 dir = new Vec3(target.getX() - this.eagle.getX(), target.getY() - this.eagle.getY(), target.getZ() - this.eagle.getZ()).normalize();
                this.eagle.setDeltaMovement(dir.scale(0.45D));

                if (this.eagle.distanceToSqr(target) < 4.0D) {
                    if (this.eagle.level() instanceof ServerLevel serverLevel) {
                        this.eagle.doHurtTarget(serverLevel, target);
                    }
                    if (!target.isAlive()) {
                        this.eagle.eatingTicks = 100;
                    }
                }
            }
        }
    }

    static class EagleRandomFlyGoal extends Goal {
        private final GiantEagleEntity eagle;

        public EagleRandomFlyGoal(GiantEagleEntity eagle) {
            this.eagle = eagle;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.eagle.isVehicle() && !this.eagle.isOrderedToSit() && 
                   (this.eagle.getNavigation().isDone() || this.eagle.random.nextInt(30) == 0);
        }

        @Override
        public void start() {
            Vec3 targetPos = this.getRandomFlightPos();
            if (targetPos != null) {
                this.eagle.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.2D);
            }
        }

        @Nullable
        private Vec3 getRandomFlightPos() {
            Vec3 look = this.eagle.getViewVector(0.0F);
            double rx = this.eagle.getX() + look.x * 16.0D + (this.eagle.random.nextDouble() - 0.5D) * 16.0D;
            double ry = this.eagle.getY() + (this.eagle.random.nextDouble() - 0.5D) * 8.0D;
            double rz = this.eagle.getZ() + look.z * 16.0D + (this.eagle.random.nextDouble() - 0.5D) * 16.0D;

            if (ry < this.eagle.level().getMinY() + 5) ry += 10;
            return new Vec3(rx, ry, rz);
        }
    }
}
