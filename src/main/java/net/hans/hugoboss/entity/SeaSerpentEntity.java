package net.hans.hugoboss.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SeaSerpentEntity extends Monster {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(),
            BossEvent.BossBarColor.BLUE,
            BossEvent.BossBarOverlay.PROGRESS
    );

    private final Map<UUID, Float> swallowedPlayerDamage = new HashMap<>();
    private final Map<UUID, Long> lastSelfDamageTime = new HashMap<>();
    private final Map<UUID, Long> lastHitTime = new HashMap<>();
    private final Map<UUID, Long> swallowStartTime = new HashMap<>();

    public SeaSerpentEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.moveControl = new SeaSerpentMoveControl(this);
        var scaleAttr = this.getAttribute(Attributes.SCALE);
        if (scaleAttr != null) {
            scaleAttr.setBaseValue(4.0D); // Huge boss scale
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new SwallowPlayerGoal(this));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            // Swallowing boat mechanic
            LivingEntity target = this.getTarget();
            if (target instanceof Player player && target.isAlive()) {
                if (player.getVehicle() instanceof Boat boat && this.distanceToSqr(player) <= 8.0 * 8.0) {
                    boat.discard(); // destroy the boat
                    this.playSound(SoundEvents.GENERIC_EAT, 2.0F, 0.8F);
                    this.playSound(SoundEvents.SHIELD_BREAK, 1.5F, 0.5F); // boat breaking sound
                    player.startRiding(this, true); // Swallow player!
                }
            }

            // Manage swallowed passengers
            long now = this.level().getGameTime();
            for (Entity passenger : this.getPassengers()) {
                if (passenger instanceof ServerPlayer player) {
                    UUID uuid = player.getUUID();
                     // Apply effects
                     player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false));
                     player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));

                     // Slowly deplete air supply (1 bubble / 30 units per minute = 1 unit per 40 ticks)
                     long startTime = this.swallowStartTime.getOrDefault(uuid, now);
                     int elapsed = (int) (now - startTime);
                     int customAir = player.getMaxAirSupply() - (elapsed / 40);
                     player.setAirSupply(Math.max(0, customAir));

                     // Track hacking (swinging arm)
                    if (player.swinging) {
                        long lastHit = this.lastHitTime.getOrDefault(uuid, 0L);
                        if (now - lastHit >= 10) { // 0.5s cooldown
                            this.lastHitTime.put(uuid, now);

                            float baseDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);

                            // Deal damage to the serpent
                            this.hurt(this.damageSources().playerAttack(player), baseDamage);

                            // Accumulate damage for escape
                            float accumulated = this.swallowedPlayerDamage.getOrDefault(uuid, 0.0F) + baseDamage;
                            this.swallowedPlayerDamage.put(uuid, accumulated);

                            // Apply self damage penalty (1 heart / 5 seconds)
                            long lastSelfDamage = this.lastSelfDamageTime.getOrDefault(uuid, 0L);
                            if (now - lastSelfDamage >= 100) { // 5 seconds
                                this.lastSelfDamageTime.put(uuid, now);
                                player.hurt(this.damageSources().generic(), 2.0F); // 1 heart of damage
                                player.playSound(SoundEvents.PLAYER_HURT, 1.0F, 1.0F);
                            }

                            // Check if they broke free (40 damage)
                            if (accumulated >= 40.0F) {
                                this.spitOut(player);
                            }
                        }
                    }
                }
            }
        }
    }

    private void spitOut(ServerPlayer player) {
        player.stopRiding();
        
        // Launch them out in the direction the serpent is facing
        Vec3 launch = this.getLookAngle().scale(1.5D).add(0.0D, 0.5D, 0.0D);
        player.setDeltaMovement(launch);
        player.hurtMarked = true;
        
        player.removeEffect(MobEffects.BLINDNESS);
        player.removeEffect(MobEffects.DARKNESS);
        
        this.playSound(SoundEvents.DOLPHIN_EAT, 2.0F, 0.8F);
        player.sendSystemMessage(Component.literal("Du lyckades hugga dig ut ur ormens mage!"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
            return false;
        }
        boolean flag = super.hurt(source, amount);
        if (flag && !this.level().isClientSide) {
            Entity attacker = source.getEntity();
            boolean isSwallowedAttacker = false;
            if (attacker instanceof Player) {
                isSwallowedAttacker = attacker.getVehicle() == this;
            }

            // Spit out all passengers if hurt significantly by an outside source or when dying
            if (!isSwallowedAttacker || this.isDeadOrDying()) {
                for (Entity passenger : this.getPassengers()) {
                    if (passenger instanceof ServerPlayer player) {
                        this.spitOut(player);
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.hasPassenger(passenger)) {
            moveFunction.accept(passenger, this.getX(), this.getY() + 0.5D, this.getZ());
        }
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (passenger instanceof ServerPlayer player) {
            this.swallowStartTime.put(player.getUUID(), this.level().getGameTime());
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof ServerPlayer player) {
            this.swallowStartTime.remove(player.getUUID());
            this.swallowedPlayerDamage.remove(player.getUUID());
            this.lastSelfDamageTime.remove(player.getUUID());
            this.lastHitTime.remove(player.getUUID());
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return false;
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (!this.level().isClientSide) {
            for (ServerPlayer player : this.bossEvent.getPlayers()) {
                this.bossEvent.removePlayer(player);
            }
        }
    }

    public static class SeaSerpentMoveControl extends MoveControl {
        private final SeaSerpentEntity serpent;

        public SeaSerpentMoveControl(SeaSerpentEntity serpent) {
            super(serpent);
            this.serpent = serpent;
        }

        @Override
        public void tick() {
            if (this.serpent.isInWater()) {
                this.serpent.setDeltaMovement(this.serpent.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
                if (this.operation == MoveControl.Operation.MOVE_TO) {
                    double dx = this.wantedX - this.serpent.getX();
                    double dy = this.wantedY - this.serpent.getY();
                    double dz = this.wantedZ - this.serpent.getZ();
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (dist < 1.0E-7D) {
                        this.serpent.setSpeed(0.0F);
                    } else {
                        double speed = this.speedModifier * this.serpent.getAttributeValue(Attributes.MOVEMENT_SPEED);
                        this.serpent.setDeltaMovement(this.serpent.getDeltaMovement().add(
                                (dx / dist) * speed * 0.1D,
                                (dy / dist) * speed * 0.1D,
                                (dz / dist) * speed * 0.1D
                        ));
                        float yaw = (float) (Math.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;
                        this.serpent.setYRot(this.rotlerp(this.serpent.getYRot(), yaw, 90.0F));
                        this.serpent.yBodyRot = this.serpent.getYRot();
                    }
                }
            } else {
                super.tick();
            }
        }
    }

    public static class SwallowPlayerGoal extends Goal {
        private final SeaSerpentEntity serpent;
        private int delayCounter;

        public SwallowPlayerGoal(SeaSerpentEntity serpent) {
            this.serpent = serpent;
            this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.serpent.getTarget();
            if (target instanceof Player player) {
                return player.isAlive() && player.getVehicle() != this.serpent;
            }
            return false;
        }

        @Override
        public void start() {
            this.delayCounter = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = this.serpent.getTarget();
            if (target != null) {
                this.serpent.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (--this.delayCounter <= 0) {
                    this.delayCounter = 10;
                    this.serpent.getNavigation().moveTo(target, 1.2D);
                }

                double dist = this.serpent.distanceToSqr(target);
                if (dist <= 8.0 * 8.0) { // 8 blocks distance
                    if (!this.serpent.level().isClientSide) {
                        if (target.getVehicle() instanceof Boat boat) {
                            boat.discard();
                        }
                        this.serpent.playSound(SoundEvents.GENERIC_EAT, 2.0F, 0.8F);
                        this.serpent.playSound(SoundEvents.SHIELD_BREAK, 1.5F, 0.5F);
                        target.startRiding(this.serpent, true);
                    }
                }
            }
        }
    }
}
