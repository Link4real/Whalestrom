package com.link.whalestrom.entity.client;

import com.link.whalestrom.entity.animation.ModAnimations;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
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

		ModelPartData arms = body.addChild("arms", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -5.0F, 8.0F));

		ModelPartData arm_left = arms.addChild("arm_left", ModelPartBuilder.create(), ModelTransform.of(-15.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r1 = arm_left.addChild("cube_r1", ModelPartBuilder.create().uv(30, 46).cuboid(11.0F, -2.0F, 63.0F, 12.0F, 1.0F, 3.0F, new Dilation(0.0F))
				.uv(0, 30).cuboid(11.0F, -2.0F, 56.0F, 19.0F, 1.0F, 7.0F, new Dilation(0.0F))
				.uv(0, 140).cuboid(11.0F, -2.0F, 11.0F, 22.0F, 1.0F, 45.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData arm_right = arms.addChild("arm_right", ModelPartBuilder.create(), ModelTransform.of(16.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r2 = arm_right.addChild("cube_r2", ModelPartBuilder.create().uv(0, 22).cuboid(11.0F, -2.0F, -44.0F, 19.0F, 1.0F, 7.0F, new Dilation(0.0F))
				.uv(25, 42).cuboid(11.0F, -2.0F, -47.0F, 12.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-20.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r3 = arm_right.addChild("cube_r3", ModelPartBuilder.create().uv(105, 0).cuboid(-33.0F, -2.0F, -8.0F, 22.0F, 1.0F, 45.0F, new Dilation(0.0F)), ModelTransform.of(-20.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData torso = body.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-11.0F, -22.0F, -24.0F, 22.0F, 21.0F, 61.0F, new Dilation(0.0F))
				.uv(93, 83).cuboid(-9.0F, -23.0F, -22.0F, 18.0F, 1.0F, 57.0F, new Dilation(0.0F))
				.uv(0, 82).cuboid(-9.0F, -1.0F, -22.0F, 18.0F, 1.0F, 57.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(89, 141).cuboid(-10.0F, -18.0F, -45.0F, 20.0F, 17.0F, 21.0F, new Dilation(0.0F))
				.uv(0, 38).cuboid(-9.0F, -19.0F, -27.0F, 18.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData lower_body = body.addChild("lower_body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData tail = lower_body.addChild("tail", ModelPartBuilder.create().uv(155, 163).cuboid(-9.0F, -17.0F, 37.0F, 18.0F, 15.0F, 16.0F, new Dilation(0.0F))
				.uv(0, 42).cuboid(-5.0F, -10.0F, 53.0F, 10.0F, 6.0F, 5.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-7.0F, -25.0F, 53.0F, 14.0F, 15.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData fin_right = lower_body.addChild("fin_right", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 13.0F, 1.0F));

		ModelPartData cube_r4 = fin_right.addChild("cube_r4", ModelPartBuilder.create().uv(171, 141).cuboid(14.0F, -25.0F, 60.0F, 11.0F, 1.0F, 21.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2399F, -0.4253F, -0.1006F));

		ModelPartData fin_left = lower_body.addChild("fin_left", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 13.0F, 1.0F));

		ModelPartData cube_r5 = fin_left.addChild("cube_r5", ModelPartBuilder.create().uv(145, 61).cuboid(-25.0F, -25.0F, 60.0F, 11.0F, 1.0F, 21.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2399F, 0.4253F, 0.1006F));
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

		this.head.yaw = headYaw * ((float)Math.PI / 180);
		this.head.pitch = headPitch * ((float)Math.PI / 180);
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