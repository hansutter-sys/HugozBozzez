package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.client.model.GorillaModel;
import net.hans.hugoboss.entity.GorillaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class GorillaRenderer extends MobRenderer<GorillaEntity, LivingEntityRenderState, GorillaModel> {
    private static final Identifier TEXTURE = 
            Identifier.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/gorilla.png");

    public GorillaRenderer(EntityRendererProvider.Context context) {
        super(context, new GorillaModel(context.bakeLayer(HugoBOSSClient.GORILLA_MODEL_LAYER)), 0.8F);
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
