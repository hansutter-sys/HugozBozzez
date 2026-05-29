package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.entity.SeaSerpentEntity;
import net.hans.hugoboss.client.model.SerpentModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SeaSerpentRenderer extends MobRenderer<SeaSerpentEntity, SerpentModel<SeaSerpentEntity>> {
    private static final ResourceLocation TEXTURE = 
            ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/sea_serpent.png");

    public SeaSerpentRenderer(EntityRendererProvider.Context context) {
        super(context, new SerpentModel<>(context.bakeLayer(HugoBOSSClient.SERPENT_MODEL_LAYER)), 1.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SeaSerpentEntity entity) {
        return TEXTURE;
    }
}
