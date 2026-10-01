package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.client.model.MonkeyModel;
import net.hans.hugoboss.entity.MonkeyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class MonkeyRenderer extends MobRenderer<MonkeyEntity, LivingEntityRenderState, MonkeyModel> {
    private static final Identifier TEXTURE = 
            Identifier.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/monkey.png");

    public MonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new MonkeyModel(context.bakeLayer(HugoBOSSClient.MONKEY_MODEL_LAYER)), 0.4F);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
