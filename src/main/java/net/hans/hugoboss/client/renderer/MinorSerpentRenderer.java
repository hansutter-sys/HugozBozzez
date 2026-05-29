package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.entity.MinorSerpentEntity;
import net.hans.hugoboss.client.model.SerpentModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MinorSerpentRenderer extends MobRenderer<MinorSerpentEntity, SerpentModel<MinorSerpentEntity>> {
    private static final ResourceLocation TEXTURE = 
            ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/minor_serpent.png");

    public MinorSerpentRenderer(EntityRendererProvider.Context context) {
        super(context, new SerpentModel<>(context.bakeLayer(HugoBOSSClient.SERPENT_MODEL_LAYER)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(MinorSerpentEntity entity) {
        return TEXTURE;
    }
}
