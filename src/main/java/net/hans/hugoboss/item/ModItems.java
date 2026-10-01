package net.hans.hugoboss.item;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HugoBOSS.MODID);

    public static final DeferredItem<CreeperHeartItem> CREEPER_HEART = ITEMS.register("creeper_heart", 
            () -> new CreeperHeartItem(new Item.Properties()));

    public static final DeferredItem<TntStaffItem> TNT_STAFF = ITEMS.register("tnt_staff", 
            () -> new TntStaffItem(new Item.Properties()));

    public static final DeferredItem<Item> MEGA_CREEPER_SPAWN_EGG = ITEMS.register("mega_creeper_spawn_egg", 
            () -> new SpawnEggItem(ModEntities.MEGA_CREEPER.get(), new Item.Properties()));

    public static final DeferredItem<Item> SEA_SERPENT_SPAWN_EGG = ITEMS.register("sea_serpent_spawn_egg", 
            () -> new SpawnEggItem(ModEntities.SEA_SERPENT.get(), new Item.Properties()));

    public static final DeferredItem<Item> MINOR_SERPENT_SPAWN_EGG = ITEMS.register("minor_serpent_spawn_egg", 
            () -> new SpawnEggItem(ModEntities.MINOR_SERPENT.get(), new Item.Properties()));

    public static final DeferredItem<Item> GORILLA_SPAWN_EGG = ITEMS.register("gorilla_spawn_egg", 
            () -> new SpawnEggItem(ModEntities.GORILLA.get(), new Item.Properties()));

    public static final DeferredItem<Item> MONKEY_SPAWN_EGG = ITEMS.register("monkey_spawn_egg", 
            () -> new SpawnEggItem(ModEntities.MONKEY.get(), new Item.Properties()));

    public static final DeferredItem<Item> GIANT_EAGLE_SPAWN_EGG = ITEMS.register("giant_eagle_spawn_egg", 
            () -> new SpawnEggItem(ModEntities.GIANT_EAGLE.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
