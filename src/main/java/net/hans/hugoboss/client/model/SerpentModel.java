package net.hans.hugoboss.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SerpentModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart[] bodies = new ModelPart[13];

    public SerpentModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        
        ModelPart currentParent = this.head;
        for (int i = 0; i < 13; i++) {
            this.bodies[i] = currentParent.getChild("body" + i);
            currentParent = this.bodies[i];
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Head (child of root)
        PartDefinition headPart = partdefinition.addOrReplaceChild("head", 
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F), 
                PartPose.offset(0.0F, 20.0F, -24.0F));

        // Nest body segments in a chain (each body segment is a child of the previous one)
        PartDefinition currentParent = headPart;
        for (int i = 0; i < 13; i++) {
            int texX = (i % 2 == 0) ? 0 : 24;
            float offsetZ = (i == 0) ? 0.0F : 8.0F;
            currentParent = currentParent.addOrReplaceChild("body" + i, 
                    CubeListBuilder.create().texOffs(texX, 20).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.0F), 
                    PartPose.offset(0.0F, 0.0F, offsetZ));
        }

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * ((float)Math.PI / 180F);
        this.head.xRot = state.xRot * ((float)Math.PI / 180F);

        // Body wave animation: slither speed scales with movement speed, with a base idle wave
        float speed = 0.15F + state.walkAnimationSpeed * 0.15F;
        float magnitude = 0.1F + state.walkAnimationSpeed * 0.12F;

        for (int i = 0; i < 13; i++) {
            ModelPart bodyPart = this.bodies[i];
            if (bodyPart != null) {
                // Slithering wave relative to parent
                float slither = (float) Math.sin(state.ageInTicks * speed - i * 0.4F) * magnitude;
                // Slower idle wave
                float idleBend = (float) Math.sin(state.ageInTicks * 0.03F - i * 0.2F) * 0.05F;
                
                bodyPart.yRot = slither + idleBend;
                
                // Add a very subtle vertical wave to look 3D
                bodyPart.xRot = (float) Math.cos(state.ageInTicks * 0.04F - i * 0.3F) * 0.03F;
            }
        }
    }
}
