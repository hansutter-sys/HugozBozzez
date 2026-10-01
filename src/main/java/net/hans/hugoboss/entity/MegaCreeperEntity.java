package net.hans.hugoboss.entity;

import net.hans.hugoboss.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

public class MegaCreeperEntity extends Creeper {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            UUID.randomUUID(),
            this.getDisplayName(),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.PROGRESS
    );

    private int minionCooldown = 100; // Initial cooldown of 5 seconds

    public MegaCreeperEntity(EntityType<? extends Creeper> type, Level level) {
        super(type, level);
        var scaleAttr = this.getAttribute(Attributes.SCALE);
        if (scaleAttr != null) {
            scaleAttr.setBaseValue(3.5D);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Add the custom TNT attack goal
        this.goalSelector.addGoal(2, new MegaCreeperTntAttackGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            // Handle minion spawning in combat
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                if (this.minionCooldown > 0) {
                    this.minionCooldown--;
                } else {
                    this.spawnMinions(target);
                    // Cooldown 15-20 seconds (300 to 400 ticks)
                    this.minionCooldown = 300 + this.random.nextInt(101);
                }
            }
        }
    }

    private void spawnMinions(LivingEntity target) {
        Level level = this.level();
        for (int i = 0; i < 2; i++) {
            double x = this.getX() + (this.random.nextDouble() - 0.5D) * 6.0D;
            double y = this.getY();
            double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 6.0D;

            Creeper minion = EntityTypes.CREEPER.create(level, EntitySpawnReason.MOB_SUMMONED);
            if (minion != null) {
                minion.snapTo(x, y, z, this.random.nextFloat() * 360.0F, 0.0F);

                var scaleAttr = minion.getAttribute(Attributes.SCALE);
                if (scaleAttr != null) {
                    scaleAttr.setBaseValue(0.5D);
                }

                minion.setCustomName(Component.literal("Mega Creeper Minion"));
                minion.setTarget(target);

                level.addFreshEntity(minion);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                            x, y + 0.5, z,
                            5, 0.2, 0.2, 0.2, 0.0
                    );
                }
            }
        }
        this.playSound(net.minecraft.sounds.SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
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
        if (!this.level().isClientSide()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                // Large explosion of radius 8.0 MOB
                serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), 8.0F, Level.ExplosionInteraction.MOB);
                // Spawn the Creeper Heart item
                this.spawnAtLocation(serverLevel, ModItems.CREEPER_HEART.get());
            }

            // Clear players from boss bar
            for (ServerPlayer player : this.bossEvent.getPlayers()) {
                this.bossEvent.removePlayer(player);
            }
        }
    }

    public static class MegaCreeperTntAttackGoal extends Goal {
        private final MegaCreeperEntity boss;
        private int cooldown;

        public MegaCreeperTntAttackGoal(MegaCreeperEntity boss) {
            this.boss = boss;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.boss.getTarget();
            return target != null && target.isAlive() && this.boss.distanceToSqr(target) <= 15.0 * 15.0;
        }

        @Override
        public void start() {
            this.cooldown = 40 + this.boss.getRandom().nextInt(40);
        }

        @Override
        public void tick() {
            LivingEntity target = this.boss.getTarget();
            if (target == null) return;

            this.boss.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.cooldown > 0) {
                this.cooldown--;
            } else {
                this.throwTnt(target);
                this.cooldown = 80 + this.boss.getRandom().nextInt(41); // 4-6 seconds
            }
        }

        private void throwTnt(LivingEntity target) {
            Level level = this.boss.level();
            if (level.isClientSide()) return;

            double spawnY = this.boss.getY() + (this.boss.getBbHeight() * 0.8D);
            PrimedTnt tnt = new PrimedTnt(level, this.boss.getX(), spawnY, this.boss.getZ(), this.boss);
            tnt.setFuse(60); // fuse of 60 ticks (3 seconds)

            double dx = target.getX() - this.boss.getX();
            double dy = target.getY() + (double) target.getEyeHeight() - spawnY;
            double dz = target.getZ() - this.boss.getZ();
            double horizDist = Math.sqrt(dx * dx + dz * dz);

            double speed = 0.2D + horizDist * 0.05D;
            if (speed > 1.2D) speed = 1.2D;

            double angle = 0.4D; // 23 degrees launch angle
            double scale = Math.sqrt(dx * dx + dz * dz);
            if (scale == 0) scale = 1.0D;

            double vx = (dx / scale) * speed * Math.cos(angle);
            double vz = (dz / scale) * speed * Math.cos(angle);
            double vy = speed * Math.sin(angle) + dy * 0.1D;

            tnt.setDeltaMovement(vx, vy, vz);
            level.addFreshEntity(tnt);

            this.boss.playSound(net.minecraft.sounds.SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
        }
    }
}
