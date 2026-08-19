package net.hans.hugoboss;

import net.hans.hugoboss.entity.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = HugoBOSS.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = HugoBOSS.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class HugoBOSSClient {
    public HugoBOSSClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        HugoBOSS.LOGGER.info("HELLO FROM CLIENT SETUP");
        HugoBOSS.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    public static final net.minecraft.client.model.geom.ModelLayerLocation SERPENT_MODEL_LAYER = 
            new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "serpent"), "main");

    public static final net.minecraft.client.model.geom.ModelLayerLocation GORILLA_MODEL_LAYER = 
            new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "gorilla"), "main");

    public static final net.minecraft.client.model.geom.ModelLayerLocation MONKEY_MODEL_LAYER = 
            new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "monkey"), "main");

    public static final net.minecraft.client.model.geom.ModelLayerLocation EAGLE_MODEL_LAYER = 
            new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "giant_eagle"), "main");

    @SubscribeEvent
    static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SERPENT_MODEL_LAYER, net.hans.hugoboss.client.model.SerpentModel::createBodyLayer);
        event.registerLayerDefinition(GORILLA_MODEL_LAYER, net.hans.hugoboss.client.model.GorillaModel::createBodyLayer);
        event.registerLayerDefinition(MONKEY_MODEL_LAYER, net.hans.hugoboss.client.model.MonkeyModel::createBodyLayer);
        event.registerLayerDefinition(EAGLE_MODEL_LAYER, net.hans.hugoboss.client.model.EagleModel::createBodyLayer);
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MEGA_CREEPER.get(), CreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_SERPENT.get(), net.hans.hugoboss.client.renderer.SeaSerpentRenderer::new);
        event.registerEntityRenderer(ModEntities.MINOR_SERPENT.get(), net.hans.hugoboss.client.renderer.MinorSerpentRenderer::new);
        event.registerEntityRenderer(ModEntities.GORILLA.get(), net.hans.hugoboss.client.renderer.GorillaRenderer::new);
        event.registerEntityRenderer(ModEntities.MONKEY.get(), net.hans.hugoboss.client.renderer.MonkeyRenderer::new);
        event.registerEntityRenderer(ModEntities.GIANT_EAGLE.get(), net.hans.hugoboss.client.renderer.EagleRenderer::new);
    }
}

