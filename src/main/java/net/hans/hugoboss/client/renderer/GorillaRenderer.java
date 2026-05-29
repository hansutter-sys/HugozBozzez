package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.entity.GorillaEntity;
import net.hans.hugoboss.client.model.GorillaModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GorillaRenderer extends MobRenderer<GorillaEntity, GorillaModel<GorillaEntity>> {
    private static final ResourceLocation TEXTURE = 
            ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/gorilla.png");

    public GorillaRenderer(EntityRendererProvider.Context context) {
        super(context, new GorillaModel<>(context.bakeLayer(HugoBOSSClient.GORILLA_MODEL_LAYER)), 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(GorillaEntity entity) {
        return TEXTURE;
    }
}
