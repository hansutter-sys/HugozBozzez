package net.hans.hugoboss.entity;

import net.hans.hugoboss.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GorillaEntity extends Monster {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS
    );

    private int minionCooldown = 100; // Cooldown for spawning monkeys
    private int leapCooldown = 40;     // Cooldown for tree-leap

    public GorillaEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        var scaleAttr = this.getAttribute(Attributes.SCALE);
        if (scaleAttr != null) {
            scaleAttr.setBaseValue(4.0D); // Large size like King Kong
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean onClimbable() {
        // Enables gorilla to climb tree trunks, leaves, and vertical blocks
        return this.horizontalCollision;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                // 1. Spawning Monkeys (minions)
                if (this.minionCooldown > 0) {
                    this.minionCooldown--;
                } else {
                    this.spawnMonkeys(target);
                    this.minionCooldown = 300 + this.random.nextInt(201); // 15-25 seconds cooldown
                }

                // 2. Tree Leap / Jump Attack (if gorilla is above the target)
                if (this.leapCooldown > 0) {
                    this.leapCooldown--;
                } else {
                    double dy = this.getY() - target.getY();
                    double dx = target.getX() - this.getX();
                    double dz = target.getZ() - this.getZ();
                    double distH = Math.sqrt(dx * dx + dz * dz);

                    // If gorilla is at least 1.5 blocks above target and within 7 blocks horizontally
                    if (dy > 1.5D && distH < 7.0D && distH > 1.0D) {
                        this.leapTowards(target);
                        this.leapCooldown = 120 + this.random.nextInt(61); // 6-9 seconds cooldown
                    }
                }
            }
        }
    }

    private void spawnMonkeys(LivingEntity target) {
        Level level = this.level();
        int monkeyCount = 1 + this.random.nextInt(2); // Spawns 1 or 2 monkeys

        for (int i = 0; i < monkeyCount; i++) {
            double x = this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D;
            double y = this.getY();
            double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D;

            MonkeyEntity monkey = ModEntities.MONKEY.get().create(level);
            if (monkey != null) {
                monkey.moveTo(x, y, z, this.random.nextFloat() * 360.0F, 0.0F);
                monkey.setTarget(target);
                level.addFreshEntity(monkey);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.CLOUD,
                            x, y + 0.5D, z,
                            10, 0.2D, 0.2D, 0.2D, 0.02D
                    );
                }
            }
        }
        this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.2F, 0.8F);
    }

    private void leapTowards(LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double distH = Math.sqrt(dx * dx + dz * dz);

        if (distH > 0.1D) {
            double speed = 0.9D;
            // Launch forward horizontally and slightly upward to clear blocks/obstacles
            this.setDeltaMovement(new Vec3(dx / distH * speed, 0.4D, dz / distH * speed));
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 0.8F);
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
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
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
}
