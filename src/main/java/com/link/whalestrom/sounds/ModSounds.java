package com.link.whalestrom.sounds;

import com.link.whalestrom.Whalestrom;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

   public static final SoundEvent NORHVAL_AMBIENT = registerSoundEvent("norhval_ambient");
   public static final SoundEvent NORHVAL_DEATH = registerSoundEvent("norhval_death");
   public static final SoundEvent NORHVAL_HURT = registerSoundEvent("norhval_hurt");
   public static final SoundEvent CETACEAN_STAFF_SHOOT = registerSoundEvent("cetacean_staff_shoot");
   public static final SoundEvent WHALE_TOOTH_THROWN = registerSoundEvent("whale_tooth_thrown");
    private static SoundEvent registerSoundEvent(String name) {
        Identifier identifier = new Identifier(Whalestrom.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }
    public static void registerSounds() {
        Whalestrom.LOGGER.info("Registering Sounds for " + Whalestrom.MOD_ID);
    }
}
