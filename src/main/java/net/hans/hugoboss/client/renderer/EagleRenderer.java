package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.client.model.EagleModel;
import net.hans.hugoboss.entity.GiantEagleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EagleRenderer extends MobRenderer<GiantEagleEntity, EagleModel<GiantEagleEntity>> {
    private static final ResourceLocation TEXTURE = 
            ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/giant_eagle.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleModel<>(context.bakeLayer(HugoBOSSClient.EAGLE_MODEL_LAYER)), 1.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(GiantEagleEntity entity) {
        return TEXTURE;
    }
}
