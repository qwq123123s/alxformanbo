/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package com.manbo.v2c.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.Entity;

public class ManboDeathList {
    private static final Set<UUID> DEATH_UUIDS = Collections.newSetFromMap(new ConcurrentHashMap());
    private static final Map<Class<?>, Long> DEATH_CLASSES = new ConcurrentHashMap();
    private static final Set<UUID> UUID_BLACKLIST = Collections.newSetFromMap(new ConcurrentHashMap());

    public static void addDeath(Entity entity) {
        if (entity == null) {
            return;
        }
        UUID uuid = entity.getUUID();
        if (uuid != null) {
            DEATH_UUIDS.add(uuid);
            UUID_BLACKLIST.add(uuid);
        }
        DEATH_CLASSES.put(entity.getClass(), -1L);
    }

    public static boolean isDead(Entity entity) {
        if (entity == null) {
            return false;
        }
        UUID uuid = entity.getUUID();
        if (uuid != null && DEATH_UUIDS.contains(uuid)) {
            return true;
        }
        Long ticks = DEATH_CLASSES.get(entity.getClass());
        return ticks != null && (ticks < 0L || ticks > 0L);
    }

    public static boolean isUUIDBlacklisted(UUID uuid) {
        return uuid != null && UUID_BLACKLIST.contains(uuid);
    }

    public static void clear() {
        DEATH_UUIDS.clear();
        DEATH_CLASSES.clear();
        UUID_BLACKLIST.clear();
    }
}

