package net.hans.hugoboss.item;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HugoBOSS.MODID);

    public static final DeferredItem<CreeperHeartItem> CREEPER_HEART = ITEMS.register("creeper_heart", 
            () -> new CreeperHeartItem(new Item.Properties()));

    public static final DeferredItem<TntStaffItem> TNT_STAFF = ITEMS.register("tnt_staff", 
            () -> new TntStaffItem(new Item.Properties()));

    public static final DeferredItem<Item> MEGA_CREEPER_SPAWN_EGG = ITEMS.register("mega_creeper_spawn_egg", 
            () -> new DeferredSpawnEggItem(ModEntities.MEGA_CREEPER, 0x0DA70B, 0xFF0000, new Item.Properties()));

    public static final DeferredItem<Item> SEA_SERPENT_SPAWN_EGG = ITEMS.register("sea_serpent_spawn_egg", 
            () -> new DeferredSpawnEggItem(ModEntities.SEA_SERPENT, 0x0A5F9E, 0x00D2F0, new Item.Properties()));

    public static final DeferredItem<Item> MINOR_SERPENT_SPAWN_EGG = ITEMS.register("minor_serpent_spawn_egg", 
            () -> new DeferredSpawnEggItem(ModEntities.MINOR_SERPENT, 0x08422A, 0x00FF8C, new Item.Properties()));

    public static final DeferredItem<Item> GORILLA_SPAWN_EGG = ITEMS.register("gorilla_spawn_egg", 
            () -> new DeferredSpawnEggItem(ModEntities.GORILLA, 0x3B2D22, 0xFFD700, new Item.Properties()));

    public static final DeferredItem<Item> MONKEY_SPAWN_EGG = ITEMS.register("monkey_spawn_egg", 
            () -> new DeferredSpawnEggItem(ModEntities.MONKEY, 0x6E4E37, 0xA67049, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
