package com.link.whalestrom.init;

import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeybindsInit {

    public static KeyBinding flyDown;

    public static void init() {
        flyDown = new KeyBinding("key.whalestrom.flydown", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, "category.whalestrom.keybinds");

        KeyBindingHelper.registerKeyBinding(flyDown);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                    while (flyDown.wasPressed()) {
                        assert client.player != null;
                        NorhvalEntity.flyWhaleDown(client.player, flyDown.getBoundKeyTranslationKey());
                        return;
                    }
                }
        );
    }
}