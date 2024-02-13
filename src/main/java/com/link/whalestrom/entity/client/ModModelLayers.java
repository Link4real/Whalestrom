package com.link.whalestrom.entity.client;

import com.link.whalestrom.Whalestrom;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer NORHVAL =
            new EntityModelLayer(new Identifier(Whalestrom.MOD_ID, "norhval"), "main");
    public static final EntityModelLayer TOOTH_PROJECTILE =
            new EntityModelLayer(new Identifier(Whalestrom.MOD_ID, "tooth_projectile"), "main");
}
