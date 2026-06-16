/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.RegistryObject
 */
package com.manbo.v2c;

import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create((IForgeRegistry)ForgeRegistries.SOUND_EVENTS, (String)"manbov2c");
    private static final Random RANDOM = new Random();
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_1 = ModSounds.register("manbo_final_sword.hit.1");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_2 = ModSounds.register("manbo_final_sword.hit.2");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_3 = ModSounds.register("manbo_final_sword.hit.3");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_4 = ModSounds.register("manbo_final_sword.hit.4");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_5 = ModSounds.register("manbo_final_sword.hit.5");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_6 = ModSounds.register("manbo_final_sword.hit.6");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_7 = ModSounds.register("manbo_final_sword.hit.7");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_8 = ModSounds.register("manbo_final_sword.hit.8");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_9 = ModSounds.register("manbo_final_sword.hit.9");
    public static final RegistryObject<SoundEvent> FINAL_SWORD_HIT_10 = ModSounds.register("manbo_final_sword.hit.10");

    private static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation("manbov2c", name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent((ResourceLocation)id));
    }

    public static SoundEvent getRandomHitSound() {
        int index = RANDOM.nextInt(10);
        return switch (index) {
            case 0 -> (SoundEvent)FINAL_SWORD_HIT_1.get();
            case 1 -> (SoundEvent)FINAL_SWORD_HIT_2.get();
            case 2 -> (SoundEvent)FINAL_SWORD_HIT_3.get();
            case 3 -> (SoundEvent)FINAL_SWORD_HIT_4.get();
            case 4 -> (SoundEvent)FINAL_SWORD_HIT_5.get();
            case 5 -> (SoundEvent)FINAL_SWORD_HIT_6.get();
            case 6 -> (SoundEvent)FINAL_SWORD_HIT_7.get();
            case 7 -> (SoundEvent)FINAL_SWORD_HIT_8.get();
            case 8 -> (SoundEvent)FINAL_SWORD_HIT_9.get();
            case 9 -> (SoundEvent)FINAL_SWORD_HIT_10.get();
            default -> (SoundEvent)FINAL_SWORD_HIT_1.get();
        };
    }
}

