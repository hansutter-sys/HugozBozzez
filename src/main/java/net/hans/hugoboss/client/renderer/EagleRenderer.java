package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.client.model.EagleModel;
import net.hans.hugoboss.client.renderer.state.EagleRenderState;
import net.hans.hugoboss.entity.GiantEagleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class EagleRenderer extends MobRenderer<GiantEagleEntity, EagleRenderState, EagleModel> {
    private static final Identifier TEXTURE = 
            Identifier.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/giant_eagle.png");

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleModel(context.bakeLayer(HugoBOSSClient.EAGLE_MODEL_LAYER)), 1.2F);
    }

    @Override
    public EagleRenderState createRenderState() {
        return new EagleRenderState();
    }

    @Override
    public void extractRenderState(GiantEagleEntity entity, EagleRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isFlying = entity.isEagleFlying();
    }

    @Override
    public Identifier getTextureLocation(EagleRenderState state) {
        return TEXTURE;
    }
}
