package net.hans.hugoboss.client.model;

import net.hans.hugoboss.entity.GiantEagleEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class EagleModel<T extends GiantEagleEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_wing;
    private final ModelPart left_wing;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart tail;

    public EagleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.right_wing = root.getChild("right_wing");
        this.left_wing = root.getChild("left_wing");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", 
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F) // Huvud
                        .texOffs(24, 0).addBox(-1.5F, -1.0F, -7.0F, 3.0F, 4.0F, 3.0F), // Näbb
                PartPose.offset(0.0F, 12.0F, -6.0F));

        partdefinition.addOrReplaceChild("body", 
                CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -4.0F, -7.0F, 8.0F, 8.0F, 14.0F), 
                PartPose.offset(0.0F, 14.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_wing", 
                CubeListBuilder.create().texOffs(32, 0).addBox(-14.0F, 0.0F, -5.0F, 14.0F, 2.0F, 10.0F), 
                PartPose.offset(-4.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_wing", 
                CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, 0.0F, -5.0F, 14.0F, 2.0F, 10.0F), 
                PartPose.offset(4.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg", 
                CubeListBuilder.create().texOffs(0, 38).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
                PartPose.offset(-2.0F, 18.0F, 2.0F));

        partdefinition.addOrReplaceChild("left_leg", 
                CubeListBuilder.create().texOffs(12, 38).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), 
                PartPose.offset(2.0F, 18.0F, 2.0F));

        partdefinition.addOrReplaceChild("tail", 
                CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 2.0F, 8.0F), 
                PartPose.offsetAndRotation(0.0F, 13.0F, 7.0F, -0.3F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);

        if (entity.isEagleFlying()) {
            // Vingflax under flygning
            float flap = Mth.sin(ageInTicks * 0.4F) * 0.5F;
            this.right_wing.zRot = -0.3F - flap;
            this.left_wing.zRot = 0.3F + flap;

            this.right_leg.xRot = 0.6F;
            this.left_leg.xRot = 0.6F;
        } else {
            // På marken: Fällda vingar och gång-animation
            this.right_wing.zRot = -1.2F;
            this.left_wing.zRot = 1.2F;

            this.right_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.left_leg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        }
    }
}
