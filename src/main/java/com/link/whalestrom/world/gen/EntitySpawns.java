package com.link.whalestrom.world.gen;

import com.link.whalestrom.entity.ModEntities;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;

public class EntitySpawns {

    public static void addSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.PLAINS),
                SpawnGroup.CREATURE, ModEntities.NORHVAL, 20, 1, 4);


        SpawnRestriction.register(ModEntities.NORHVAL, SpawnRestriction.Location.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NorhvalEntity::canSpawn);


    }
}
