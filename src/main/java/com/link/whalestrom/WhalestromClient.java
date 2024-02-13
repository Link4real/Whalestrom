package com.link.whalestrom;

import com.link.whalestrom.entity.ModEntities;
import com.link.whalestrom.entity.client.ModModelLayers;
import com.link.whalestrom.entity.client.norhval.NorhvalModel;
import com.link.whalestrom.entity.client.norhval.NorhvalRenderer;
import com.link.whalestrom.init.KeybindsInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class WhalestromClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NORHVAL, NorhvalModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.NORHVAL, NorhvalRenderer::new);
        EntityRendererRegistry.register(ModEntities.THROWN_TOOTH_PROJECTILE, FlyingItemEntityRenderer::new);
        KeybindsInit.init();
    }
}