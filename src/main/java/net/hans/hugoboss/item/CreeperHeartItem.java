package net.hans.hugoboss.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CreeperHeartItem extends Item {
    public CreeperHeartItem(Item.Properties properties) {
        super(properties.rarity(Rarity.EPIC));
    }
}
