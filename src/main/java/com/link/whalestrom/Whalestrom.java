package com.link.whalestrom;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Whalestrom implements ModInitializer {
	public static final String MOD_ID = "whalestrom";
    public static final Logger LOGGER = LoggerFactory.getLogger("Whalestrom");

	@Override
	public void onInitialize() {

		LOGGER.info("Successfully loaded Whalestrom!");
	}
}