package com.link.whalestrom.entity.client;

import com.link.whalestrom.entity.animation.ModAnimations;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class NorhvalModel<T extends NorhvalEntity> extends SinglePartEntityModel<T> {
	private final ModelPart norhval;
	private final ModelPart head;
	public NorhvalModel(ModelPart root) {
		this.norhval = root.getChild("norhval");
		this.head = norhval.getChild("body");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData norhval = modelPartData.addChild("norhval", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body = norhval.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 87).cuboid(-7.0F, -11.0F, -31.0F, 14.0F, 11.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData fins = body.addChild("fins", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData fin2_r1 = fins.addChild("fin2_r1", ModelPartBuilder.create().uv(69, 0).cuboid(-21.0F, -6.0F, 29.0F, 10.0F, 1.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2182F, 0.6545F, 0.0F));

		ModelPartData fin1_r1 = fins.addChild("fin1_r1", ModelPartBuilder.create().uv(76, 69).cuboid(11.0F, -6.0F, 29.0F, 10.0F, 1.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2182F, -0.6545F, 0.0F));

		ModelPartData arms = body.addChild("arms", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData arm2_r1 = arms.addChild("arm2_r1", ModelPartBuilder.create().uv(0, 51).cuboid(-39.0F, -4.0F, -13.0F, 30.0F, 1.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, 0.0F, -5.0F, -0.0752F, 0.3976F, -0.1109F));

		ModelPartData arm1_r1 = arms.addChild("arm1_r1", ModelPartBuilder.create().uv(0, 69).cuboid(-4.0F, -4.0F, -8.0F, 30.0F, 1.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, 0.0F, -5.0F, -0.0752F, -0.5187F, 0.1509F));

		ModelPartData torso = body.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -14.0F, -19.0F, 16.0F, 14.0F, 37.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(52, 88).cuboid(-5.0F, -11.0F, 18.0F, 10.0F, 9.0F, 13.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.0F, -12.0F, 31.0F, 10.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void setAngles(NorhvalEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		this.setHeadAngles(netHeadYaw, headPitch);

		this.animateMovement(ModAnimations.NORHVAL_FLYING, limbSwing, limbSwingAmount, 2f, 2.5f);
		this.updateAnimation(entity.idleAnimationState, ModAnimations.NORHVAL_IDLE, ageInTicks, 1f);
	}
	private void setHeadAngles(float headYaw, float headPitch) {
		headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
		headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

		this.head.yaw = headYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		norhval.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return norhval;
	}
}