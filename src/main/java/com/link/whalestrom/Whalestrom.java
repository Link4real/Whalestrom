package com.link.whalestrom;

import com.link.whalestrom.entity.ModEntities;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Whalestrom implements ModInitializer {
	public static final String MOD_ID = "whalestrom";
    public static final Logger LOGGER = LoggerFactory.getLogger("Whalestrom");

	@Override
	public void onInitialize() {
		NorhvalEntity.registerModEntities();
		FabricDefaultAttributeRegistry.register(ModEntities.NORHVAL, NorhvalEntity.createNorhvalAttributes());
		LOGGER.info("Successfully loaded Whalestrom!");
	}
}