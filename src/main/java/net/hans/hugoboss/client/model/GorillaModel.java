package net.hans.hugoboss.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Mob;

public class GorillaModel<T extends Mob> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public GorillaModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Head (offset upwards and forward, centered at y = -7, z = -2)
        PartDefinition head = partdefinition.addOrReplaceChild("head", 
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F), 
                PartPose.offset(0.0F, -7.0F, -2.0F));
        
        // Snout/Nose
        head.addOrReplaceChild("nose", 
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F), 
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Crown Base (sitting on top of the head)
        head.addOrReplaceChild("crown_base", 
                CubeListBuilder.create().texOffs(0, 32).addBox(-4.5F, -14.0F, -6.0F, 9.0F, 2.0F, 9.0F), 
                PartPose.offset(0.0F, 0.0F, 0.0F));
        
        // Spikes on the crown corners
        head.addOrReplaceChild("crown_spike_fl", 
                CubeListBuilder.create().texOffs(36, 32).addBox(-4.5F, -16.0F, -6.0F, 2.0F, 2.0F, 2.0F), 
                PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("crown_spike_fr", 
                CubeListBuilder.create().texOffs(36, 32).addBox(2.5F, -16.0F, -6.0F, 2.0F, 2.0F, 2.0F), 
                PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("crown_spike_bl", 
                CubeListBuilder.create().texOffs(36, 32).addBox(-4.5F, -16.0F, 1.0F, 2.0F, 2.0F, 2.0F), 
                PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("crown_spike_br", 
                CubeListBuilder.create().texOffs(36, 32).addBox(2.5F, -16.0F, 1.0F, 2.0F, 2.0F, 2.0F), 
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Body (thick, bulky gorilla chest)
        partdefinition.addOrReplaceChild("body", 
                CubeListBuilder.create().texOffs(0, 48).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F), 
                PartPose.offset(0.0F, -7.0F, 0.0F));

        // Right Arm (long knuckles-reaching arm)
        partdefinition.addOrReplaceChild("right_arm", 
                CubeListBuilder.create().texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 24.0F, 6.0F), 
                PartPose.offset(0.0F, -7.0F, 0.0F));

        // Left Arm
        partdefinition.addOrReplaceChild("left_arm", 
                CubeListBuilder.create().texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 24.0F, 6.0F), 
                PartPose.offset(0.0F, -7.0F, 0.0F));

        // Right Leg (stout and short leg)
        partdefinition.addOrReplaceChild("right_leg", 
                CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 13.0F, 5.0F), 
                PartPose.offset(-4.0F, 11.0F, 0.0F));

        // Left Leg
        partdefinition.addOrReplaceChild("left_leg", 
                CubeListBuilder.create().texOffs(60, 0).addBox(-2.5F, -3.0F, -3.0F, 6.0F, 13.0F, 5.0F), 
                PartPose.offset(4.0F, 11.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        // Knuckle walking / gorilla walk cycles
        this.right_arm.xRot = -0.4F + (float)Math.sin(limbSwing * 0.5F + (float)Math.PI) * 0.8F * limbSwingAmount;
        this.left_arm.xRot = -0.4F + (float)Math.sin(limbSwing * 0.5F) * 0.8F * limbSwingAmount;
        
        this.right_leg.xRot = (float)Math.sin(limbSwing * 0.5F) * 0.8F * limbSwingAmount;
        this.left_leg.xRot = (float)Math.sin(limbSwing * 0.5F + (float)Math.PI) * 0.8F * limbSwingAmount;
    }
}
