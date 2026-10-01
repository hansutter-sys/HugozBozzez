package net.hans.hugoboss.entity;

import net.hans.hugoboss.HugoBOSS;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = HugoBOSS.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
            DeferredRegister.create(Registries.ENTITY_TYPE, HugoBOSS.MODID);

        public static final DeferredHolder<EntityType<?>, EntityType<MegaCreeperEntity>> MEGA_CREEPER = 
            ENTITY_TYPES.register("mega_creeper", 
                    () -> EntityType.Builder.of(MegaCreeperEntity::new, MobCategory.MONSTER)
                            .sized(0.6F * 3.5F, 1.7F * 3.5F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "mega_creeper"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SeaSerpentEntity>> SEA_SERPENT = 
            ENTITY_TYPES.register("sea_serpent", 
                    () -> EntityType.Builder.of(SeaSerpentEntity::new, MobCategory.MONSTER)
                            .sized(2.0F, 2.0F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "sea_serpent"))));

    public static final DeferredHolder<EntityType<?>, EntityType<MinorSerpentEntity>> MINOR_SERPENT = 
            ENTITY_TYPES.register("minor_serpent", 
                    () -> EntityType.Builder.of(MinorSerpentEntity::new, MobCategory.MONSTER)
                            .sized(0.8F, 0.8F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "minor_serpent"))));

    public static final DeferredHolder<EntityType<?>, EntityType<GorillaEntity>> GORILLA = 
            ENTITY_TYPES.register("gorilla", 
                    () -> EntityType.Builder.of(GorillaEntity::new, MobCategory.MONSTER)
                            .sized(1.4F, 2.0F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "gorilla"))));

    public static final DeferredHolder<EntityType<?>, EntityType<MonkeyEntity>> MONKEY = 
            ENTITY_TYPES.register("monkey", 
                    () -> EntityType.Builder.of(MonkeyEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 0.8F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "monkey"))));

    public static final DeferredHolder<EntityType<?>, EntityType<GiantEagleEntity>> GIANT_EAGLE = 
            ENTITY_TYPES.register("giant_eagle", 
                    () -> EntityType.Builder.of(GiantEagleEntity::new, MobCategory.CREATURE)
                            .sized(1.8F, 2.0F)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "giant_eagle"))));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MEGA_CREEPER.get(), MegaCreeperEntity.createAttributes().build());
        event.put(SEA_SERPENT.get(), SeaSerpentEntity.createAttributes().build());
        event.put(MINOR_SERPENT.get(), MinorSerpentEntity.createAttributes().build());
        event.put(GORILLA.get(), GorillaEntity.createAttributes().build());
        event.put(MONKEY.get(), MonkeyEntity.createAttributes().build());
        event.put(GIANT_EAGLE.get(), GiantEagleEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
        event.register(
                MEGA_CREEPER.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                SEA_SERPENT.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.IN_WATER,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                MINOR_SERPENT.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.IN_WATER,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                GORILLA.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                MONKEY.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                GIANT_EAGLE.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}
