package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.client.model.SerpentModel;
import net.hans.hugoboss.entity.MinorSerpentEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class MinorSerpentRenderer extends MobRenderer<MinorSerpentEntity, LivingEntityRenderState, SerpentModel> {
    private static final Identifier TEXTURE = 
            Identifier.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/minor_serpent.png");

    public MinorSerpentRenderer(EntityRendererProvider.Context context) {
        super(context, new SerpentModel(context.bakeLayer(HugoBOSSClient.SERPENT_MODEL_LAYER)), 0.6F);
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
