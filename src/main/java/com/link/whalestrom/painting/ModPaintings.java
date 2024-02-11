package com.link.whalestrom.painting;

import com.link.whalestrom.Whalestrom;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPaintings {
    public static final PaintingVariant FLYING_WHALE = registerPainting("the_flying_whale", new PaintingVariant(16, 32));
    private static PaintingVariant registerPainting(String name, PaintingVariant paintingVariant) {
        return Registry.register(Registries.PAINTING_VARIANT, new Identifier(Whalestrom.MOD_ID, name), paintingVariant);
    }
    public static void registerPaintings() {
        Whalestrom.LOGGER.info("Registering Paintings for " + Whalestrom.MOD_ID);
    }
}
