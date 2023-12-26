package com.link.whalestrom;

import com.link.whalestrom.entity.ModEntities;
import com.link.whalestrom.entity.client.ModModelLayers;
import com.link.whalestrom.entity.client.NorhvalModel;
import com.link.whalestrom.entity.client.NorhvalRenderer;
import com.link.whalestrom.init.KeybindsInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
public class WhalestromClient implements ClientModInitializer {
    private static KeyBinding keyBinding;
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.NORHVAL, NorhvalModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.NORHVAL, NorhvalRenderer::new);
        KeybindsInit.init();
    }
}
