package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.entity.MonkeyEntity;
import net.hans.hugoboss.client.model.MonkeyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MonkeyRenderer extends MobRenderer<MonkeyEntity, MonkeyModel<MonkeyEntity>> {
    private static final ResourceLocation TEXTURE = 
            ResourceLocation.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/monkey.png");

    public MonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new MonkeyModel<>(context.bakeLayer(HugoBOSSClient.MONKEY_MODEL_LAYER)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(MonkeyEntity entity) {
        return TEXTURE;
    }
}
