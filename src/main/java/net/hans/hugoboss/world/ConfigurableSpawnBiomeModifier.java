package net.hans.hugoboss.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hans.hugoboss.Config;
import net.hans.hugoboss.entity.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public record ConfigurableSpawnBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {

    public static final MapCodec<ConfigurableSpawnBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConfigurableSpawnBiomeModifier::biomes)
    ).apply(builder, ConfigurableSpawnBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome)) {
            int weight = Config.MEGA_CREEPER_SPAWN_WEIGHT.get();
            if (weight > 0) {
                builder.getMobSpawnSettings().addSpawn(
                        MobCategory.MONSTER,
                        new MobSpawnSettings.SpawnerData(ModEntities.MEGA_CREEPER.get(), weight, 1, 1)
                );
            }
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
