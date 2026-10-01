package net.hans.hugoboss.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MonkeyEntity extends Monster {

    private int leapCooldown = 30; // Jump cooldown in ticks

    public MonkeyEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean onClimbable() {
        // Enables monkey to climb trees and leaves
        return this.horizontalCollision;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                // Leap down attack
                if (this.leapCooldown > 0) {
                    this.leapCooldown--;
                } else {
                    double dy = this.getY() - target.getY();
                    double dx = target.getX() - this.getX();
                    double dz = target.getZ() - this.getZ();
                    double distH = Math.sqrt(dx * dx + dz * dz);

                    // If monkey is higher than target and within 6 blocks horizontally
                    if (dy > 1.2D && distH < 6.0D && distH > 0.8D) {
                        this.leapTowards(target);
                        this.leapCooldown = 80 + this.random.nextInt(41); // 4-6 seconds cooldown
                    }
                }
            }
        }
    }

    private void leapTowards(LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double distH = Math.sqrt(dx * dx + dz * dz);

        if (distH > 0.1D) {
            double speed = 1.0D; // Faster speed for small monkey
            this.setDeltaMovement(new Vec3(dx / distH * speed, 0.35D, dz / distH * speed));
            this.playSound(SoundEvents.OCELOT_DEATH, 0.8F, 1.5F); // High pitched cry
        }
    }
}
