package com.link.whalestrom.entity;

import com.link.whalestrom.Whalestrom;
import com.link.whalestrom.entity.custom.NorhvalEntity;
import com.link.whalestrom.entity.custom.ToothProjectileEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<NorhvalEntity> NORHVAL = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Whalestrom.MOD_ID, "norhval"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NorhvalEntity::new)
                    .dimensions(EntityDimensions.fixed(3.5f, 1.5f)).build()); //Hitbox

    public static final EntityType<ToothProjectileEntity> THROWN_TOOTH_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Whalestrom.MOD_ID, "tooth_projectile"),
            FabricEntityTypeBuilder.<ToothProjectileEntity>create(SpawnGroup.CREATURE, ToothProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build()); //Hitbox
}
