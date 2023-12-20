package com.link.whalestrom.item;

import com.link.whalestrom.Whalestrom;
import com.link.whalestrom.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item NORHVAL_SPAWN_EGG = new SpawnEggItem(ModEntities.NORHVAL, 0x6EFFCA, 0x6EC5FF, new FabricItemSettings());
    public static final Item WHALE_TOOTH = new Item(new FabricItemSettings().maxCount(16).fireproof());
    public static final Item ARTIFACT_LEVITATION = new Item(new FabricItemSettings().maxCount(1).fireproof());
    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier(Whalestrom.MOD_ID, "norhval_spawn_egg"), NORHVAL_SPAWN_EGG);
        Registry.register(Registries.ITEM, new Identifier(Whalestrom.MOD_ID, "whale_tooth"), WHALE_TOOTH);
        Registry.register(Registries.ITEM, new Identifier(Whalestrom.MOD_ID, "artifact_of_levitation"), ARTIFACT_LEVITATION);
    }
}
