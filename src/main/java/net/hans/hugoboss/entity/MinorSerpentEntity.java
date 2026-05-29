package net.hans.hugoboss.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

public class MinorSerpentEntity extends Monster {

    public MinorSerpentEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.moveControl = new MinorSerpentMoveControl(this);
        var scaleAttr = this.getAttribute(Attributes.SCALE);
        if (scaleAttr != null) {
            scaleAttr.setBaseValue(1.5D); // Scales the segment-based model size slightly
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        if (source.is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        if (target instanceof Player player) {
            // Apply custom damage calculation to always deal at least 3 hearts of damage
            float finalDamage = 6.0F; // 3 hearts
            int armorValue = player.getArmorValue();
            if (armorValue < 20) {
                finalDamage += (20 - armorValue) * 0.4F; // scaling for weaker armors
            }
            boolean hurt = player.hurt(this.damageSources().magic(), finalDamage);
            if (hurt) {
                this.playSound(net.minecraft.sounds.SoundEvents.PHANTOM_BITE, 1.0F, 1.0F);
            }
            return hurt;
        }
        return super.doHurtTarget(target);
    }

    public static class MinorSerpentMoveControl extends MoveControl {
        private final MinorSerpentEntity serpent;

        public MinorSerpentMoveControl(MinorSerpentEntity serpent) {
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
}
