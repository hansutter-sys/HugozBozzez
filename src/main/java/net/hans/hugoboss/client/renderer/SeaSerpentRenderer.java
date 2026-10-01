package net.hans.hugoboss.client.renderer;

import net.hans.hugoboss.HugoBOSS;
import net.hans.hugoboss.HugoBOSSClient;
import net.hans.hugoboss.client.model.SerpentModel;
import net.hans.hugoboss.entity.SeaSerpentEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class SeaSerpentRenderer extends MobRenderer<SeaSerpentEntity, LivingEntityRenderState, SerpentModel> {
    private static final Identifier TEXTURE = 
            Identifier.fromNamespaceAndPath(HugoBOSS.MODID, "textures/entity/sea_serpent.png");

    public SeaSerpentRenderer(EntityRendererProvider.Context context) {
        super(context, new SerpentModel(context.bakeLayer(HugoBOSSClient.SERPENT_MODEL_LAYER)), 1.5F);
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
