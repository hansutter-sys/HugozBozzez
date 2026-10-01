package net.hans.hugoboss.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class MonkeyModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart tail;

    public MonkeyModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", 
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F), 
                PartPose.offset(0.0F, 10.0F, 0.0F));

        partdefinition.addOrReplaceChild("body", 
                CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 8.0F, 6.0F), 
                PartPose.offset(0.0F, 10.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm", 
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F), 
                PartPose.offset(-4.0F, 10.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm", 
                CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F), 
                PartPose.offset(4.0F, 10.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg", 
                CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), 
                PartPose.offset(-2.0F, 18.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg", 
                CubeListBuilder.create().texOffs(12, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F), 
                PartPose.offset(2.0F, 18.0F, 0.0F));

        partdefinition.addOrReplaceChild("tail", 
                CubeListBuilder.create().texOffs(24, 32).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F), 
                PartPose.offsetAndRotation(0.0F, 16.0F, 3.0F, -0.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * ((float)Math.PI / 180F);
        this.head.xRot = state.xRot * ((float)Math.PI / 180F);

        // Standard walking animation
        this.right_arm.xRot = (float)Math.sin(state.walkAnimationPos * 0.6662F + (float)Math.PI) * 1.4F * state.walkAnimationSpeed;
        this.left_arm.xRot = (float)Math.sin(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
        this.right_leg.xRot = (float)Math.sin(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
        this.left_leg.xRot = (float)Math.sin(state.walkAnimationPos * 0.6662F + (float)Math.PI) * 1.4F * state.walkAnimationSpeed;

        // Tail waving animation
        this.tail.yRot = (float)Math.sin(state.ageInTicks * 0.15F) * 0.2F;
    }
}
