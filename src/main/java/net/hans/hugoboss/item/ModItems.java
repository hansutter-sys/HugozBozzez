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

    public static final DeferredItem<CreeperHeartItem> CREEPER_HEART = ITEMS.registerItem("creeper_heart", 
            CreeperHeartItem::new);

    public static final DeferredItem<TntStaffItem> TNT_STAFF = ITEMS.registerItem("tnt_staff", 
            TntStaffItem::new);

    public static final DeferredItem<Item> MEGA_CREEPER_SPAWN_EGG = ITEMS.registerItem("mega_creeper_spawn_egg", 
            SpawnEggItem::new, props -> props.spawnEgg(ModEntities.MEGA_CREEPER.get()));

    public static final DeferredItem<Item> SEA_SERPENT_SPAWN_EGG = ITEMS.registerItem("sea_serpent_spawn_egg", 
            SpawnEggItem::new, props -> props.spawnEgg(ModEntities.SEA_SERPENT.get()));

    public static final DeferredItem<Item> MINOR_SERPENT_SPAWN_EGG = ITEMS.registerItem("minor_serpent_spawn_egg", 
            SpawnEggItem::new, props -> props.spawnEgg(ModEntities.MINOR_SERPENT.get()));

    public static final DeferredItem<Item> GORILLA_SPAWN_EGG = ITEMS.registerItem("gorilla_spawn_egg", 
            SpawnEggItem::new, props -> props.spawnEgg(ModEntities.GORILLA.get()));

    public static final DeferredItem<Item> MONKEY_SPAWN_EGG = ITEMS.registerItem("monkey_spawn_egg", 
            SpawnEggItem::new, props -> props.spawnEgg(ModEntities.MONKEY.get()));

    public static final DeferredItem<Item> GIANT_EAGLE_SPAWN_EGG = ITEMS.registerItem("giant_eagle_spawn_egg", 
            SpawnEggItem::new, props -> props.spawnEgg(ModEntities.GIANT_EAGLE.get()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
