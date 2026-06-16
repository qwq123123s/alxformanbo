/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  net.minecraftforge.fml.loading.FMLPaths
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.client.defense;

import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorrorSoundBlacklist {
    private static final Logger LOGGER = LoggerFactory.getLogger(HorrorSoundBlacklist.class);
    private static final Set<String> SOUND_BLACKLIST = ConcurrentHashMap.newKeySet();
    private static final Set<String> EFFECT_BLACKLIST = ConcurrentHashMap.newKeySet();
    private static final Set<String> VANILLA_NAMESPACES = new HashSet<String>();
    private static final String CONFIG_FILE = "manbov2c_horror_blacklist.json";

    public static boolean isSoundBlacklisted(String soundLocation) {
        if (soundLocation == null || soundLocation.isEmpty()) {
            return false;
        }
        if (HorrorSoundBlacklist.isVanilla(soundLocation)) {
            return false;
        }
        String lower = soundLocation.toLowerCase();
        return SOUND_BLACKLIST.stream().anyMatch(lower::contains);
    }

    public static void addSoundToBlacklist(String soundLocation) {
        if (soundLocation == null || soundLocation.isEmpty()) {
            return;
        }
        if (HorrorSoundBlacklist.isVanilla(soundLocation)) {
            return;
        }
        SOUND_BLACKLIST.add(soundLocation.toLowerCase());
        HorrorSoundBlacklist.save();
    }

    public static void addSoundsToBlacklist(Set<String> sounds) {
        if (sounds == null || sounds.isEmpty()) {
            return;
        }
        for (String s : sounds) {
            if (HorrorSoundBlacklist.isVanilla(s)) continue;
            SOUND_BLACKLIST.add(s.toLowerCase());
        }
        HorrorSoundBlacklist.save();
    }

    public static Set<String> getSoundBlacklist() {
        return Collections.unmodifiableSet(SOUND_BLACKLIST);
    }

    public static int getSoundBlacklistCount() {
        return SOUND_BLACKLIST.size();
    }

    public static boolean isEffectBlacklisted(String className) {
        if (className == null || className.isEmpty()) {
            return false;
        }
        String lower = className.toLowerCase();
        return EFFECT_BLACKLIST.stream().anyMatch(lower::contains);
    }

    public static void addEffectToBlacklist(String className) {
        if (className == null || className.isEmpty()) {
            return;
        }
        EFFECT_BLACKLIST.add(className);
        HorrorSoundBlacklist.save();
    }

    public static Set<String> getEffectBlacklist() {
        return Collections.unmodifiableSet(EFFECT_BLACKLIST);
    }

    private static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE);
    }

    public static void save() {
        Path path = HorrorSoundBlacklist.getConfigPath();
        BlacklistData data = new BlacklistData();
        data.sounds.addAll(SOUND_BLACKLIST);
        data.effects.addAll(EFFECT_BLACKLIST);
        try (OutputStreamWriter writer = new OutputStreamWriter((OutputStream)new FileOutputStream(path.toFile()), StandardCharsets.UTF_8);){
            new GsonBuilder().setPrettyPrinting().create().toJson((Object)data, (Appendable)writer);
        }
        catch (IOException e) {
            LOGGER.error("[\u6050\u6016\u9ed1\u540d\u5355] \u4fdd\u5b58\u914d\u7f6e\u6587\u4ef6\u5931\u8d25: {}", (Object)e.getMessage());
        }
    }

    public static void load() {
        Path path = HorrorSoundBlacklist.getConfigPath();
        if (!path.toFile().exists()) {
            return;
        }
        try (InputStreamReader reader = new InputStreamReader((InputStream)new FileInputStream(path.toFile()), StandardCharsets.UTF_8);){
            BlacklistData data = (BlacklistData)new GsonBuilder().create().fromJson((Reader)reader, BlacklistData.class);
            if (data != null) {
                SOUND_BLACKLIST.clear();
                EFFECT_BLACKLIST.clear();
                if (data.sounds != null) {
                    SOUND_BLACKLIST.addAll(data.sounds);
                }
                if (data.effects != null) {
                    EFFECT_BLACKLIST.addAll(data.effects);
                }
                LOGGER.info("[\u6050\u6016\u9ed1\u540d\u5355] \u5df2\u52a0\u8f7d {} \u4e2a\u97f3\u6548\u9ed1\u540d\u5355, {} \u4e2a\u7279\u6548\u9ed1\u540d\u5355", (Object)SOUND_BLACKLIST.size(), (Object)EFFECT_BLACKLIST.size());
            }
        }
        catch (Exception e) {
            LOGGER.error("[\u6050\u6016\u9ed1\u540d\u5355] \u8bfb\u53d6\u914d\u7f6e\u6587\u4ef6\u5931\u8d25: {}", (Object)e.getMessage());
        }
    }

    private static boolean isVanilla(String location) {
        for (String ns : VANILLA_NAMESPACES) {
            if (!location.startsWith(ns + ":")) continue;
            return true;
        }
        return false;
    }

    static {
        VANILLA_NAMESPACES.add("minecraft");
        VANILLA_NAMESPACES.add("ambient");
        VANILLA_NAMESPACES.add("block");
        VANILLA_NAMESPACES.add("entity");
        VANILLA_NAMESPACES.add("item");
        VANILLA_NAMESPACES.add("music");
        VANILLA_NAMESPACES.add("particle");
        VANILLA_NAMESPACES.add("ui");
        VANILLA_NAMESPACES.add("weather");
        VANILLA_NAMESPACES.add("manbov2c");
        HorrorSoundBlacklist.load();
    }

    private static class BlacklistData {
        Set<String> sounds = new HashSet<String>();
        Set<String> effects = new HashSet<String>();

        private BlacklistData() {
        }
    }
}

