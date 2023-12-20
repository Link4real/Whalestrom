package com.link.whalestrom;

import com.link.whalestrom.entity.ModEntities;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import com.link.whalestrom.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Whalestrom implements ModInitializer {
	public static final String MOD_ID = "whalestrom";
    public static final Logger LOGGER = LoggerFactory.getLogger("Whalestrom");

	public static ItemGroup ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "main"),
			FabricItemGroup.builder().displayName(Text.literal("Whalestrom")).icon(() -> new ItemStack(ModItems.NORHVAL_SPAWN_EGG)).entries((displayContext, entries) -> {
				entries.add(ModItems.NORHVAL_SPAWN_EGG);
				entries.add(ModItems.WHALE_TOOTH);

			}).build());
	@Override
	public void onInitialize() {
		NorhvalEntity.registerModEntities();
		FabricDefaultAttributeRegistry.register(ModEntities.NORHVAL, NorhvalEntity.createNorhvalAttributes());
		ModItems.registerItems();
		LOGGER.info("Successfully loaded Whalestrom!");
	}
}