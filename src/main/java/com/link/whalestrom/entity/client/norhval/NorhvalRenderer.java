package com.link.whalestrom.entity.client.norhval;

import com.link.whalestrom.Whalestrom;
import com.link.whalestrom.entity.client.ModModelLayers;
import com.link.whalestrom.entity.client.norhval.NorhvalModel;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class NorhvalRenderer extends MobEntityRenderer<NorhvalEntity, NorhvalModel<NorhvalEntity>> {
    private static final Identifier TEXTURE = new Identifier(Whalestrom.MOD_ID, "textures/entity/norhval.png");
    public NorhvalRenderer(EntityRendererFactory.Context context) {
        super(context, new NorhvalModel<>(context.getPart(ModModelLayers.NORHVAL)), 1f); //SHADOW SIZE OF ENTITY
    }

    @Override
    public Identifier getTexture(NorhvalEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(NorhvalEntity mobEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(mobEntity.isBaby()) {
            matrixStack.scale(0.3f, 0.3f, 0.3f); //Size of Baby
        }else {
            matrixStack.scale(1f, 1f, 1f); //Size of Adult
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
