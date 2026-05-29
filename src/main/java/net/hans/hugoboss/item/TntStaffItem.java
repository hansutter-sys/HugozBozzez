package net.hans.hugoboss.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TntStaffItem extends Item {
    public TntStaffItem(Item.Properties properties) {
        // Durability of 50 uses, Epic rarity
        super(properties.durability(50).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Spawn primed TNT at player's eye height
            double spawnY = player.getY() + player.getEyeHeight() - 0.3D;
            PrimedTnt tnt = new PrimedTnt(level, player.getX(), spawnY, player.getZ(), player);
            tnt.setFuse(35); // 1.75 seconds fuse

            // Calculate look direction and apply velocity
            Vec3 look = player.getLookAngle();
            tnt.setDeltaMovement(look.x * 1.5D, look.y * 1.5D + 0.2D, look.z * 1.5D);
            level.addFreshEntity(tnt);

            // Play the primed TNT sound
            level.playSound(
                    null, 
                    player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.TNT_PRIMED, 
                    SoundSource.PLAYERS, 
                    1.0F, 1.0F
            );

            // Damage the item
            EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            itemStack.hurtAndBreak(1, player, slot);

            // Apply a 1-second cooldown (20 ticks)
            player.getCooldowns().addCooldown(this, 20);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
