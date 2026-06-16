/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.entity.EntityInLevelCallback
 */
package com.manbo.v2c.coremod;

import java.lang.reflect.Field;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityInLevelCallback;

public class FinalSwordBypassHooks {
    private static volatile boolean bypassActive = false;
    private static final Field ENTITY_REMOVED = FinalSwordBypassHooks.findField(Entity.class, "removed", "f_146794_", "removed");
    private static final Field ENTITY_VALID = FinalSwordBypassHooks.findField(Entity.class, "valid", "f_146794_", "valid");
    private static final Field ENTITY_ADDED_TO_WORLD = FinalSwordBypassHooks.findField(Entity.class, "isAddedToWorld", "f_146793_", "isAddedToWorld");
    private static final Field ENTITY_LEVEL_CALLBACK = FinalSwordBypassHooks.findField(Entity.class, "levelCallback", "f_146796_", "levelCallback");
    private static final Field LIVING_DEAD = FinalSwordBypassHooks.findField(LivingEntity.class, "dead", "f_20979_", "dead");
    private static final Field LIVING_HEALTH = FinalSwordBypassHooks.findField(LivingEntity.class, "health", "f_20978_", "health");
    private static final Field LIVING_DEATH_TIME = FinalSwordBypassHooks.findField(LivingEntity.class, "deathTime", "f_20980_", "deathTime");
    private static final Field LIVING_HURT_TIME = FinalSwordBypassHooks.findField(LivingEntity.class, "hurtTime", "f_20981_", "hurtTime");

    private static Field findField(Class<?> clazz, String ... names) {
        for (String name : names) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return null;
    }

    public static void setBypassActive(boolean active) {
        bypassActive = active;
    }

    public static boolean isBypassActive() {
        return bypassActive;
    }

    public static void onEntityRemove(Entity entity) {
        if (bypassActive) {
            entity.setRemoved(Entity.RemovalReason.KILLED);
            FinalSwordBypassHooks.setBoolean(ENTITY_REMOVED, entity, true);
            FinalSwordBypassHooks.setBoolean(ENTITY_VALID, entity, false);
            FinalSwordBypassHooks.setBoolean(ENTITY_ADDED_TO_WORLD, entity, false);
            FinalSwordBypassHooks.setField(ENTITY_LEVEL_CALLBACK, entity, EntityInLevelCallback.NULL);
        }
    }

    public static boolean onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        if (bypassActive) {
            entity.setHealth(0.0f);
            return true;
        }
        return entity.hurt(source, amount);
    }

    public static void onLivingKill(LivingEntity entity) {
        if (bypassActive) {
            FinalSwordBypassHooks.setBoolean(LIVING_DEAD, entity, true);
            entity.setHealth(0.0f);
            FinalSwordBypassHooks.setInt(LIVING_DEATH_TIME, entity, 20);
            FinalSwordBypassHooks.setInt(LIVING_HURT_TIME, entity, 0);
            entity.setRemoved(Entity.RemovalReason.KILLED);
        }
    }

    public static void onSetHealth(LivingEntity entity, float health) {
        if (bypassActive) {
            FinalSwordBypassHooks.setFloat(LIVING_HEALTH, entity, 0.0f);
        } else {
            FinalSwordBypassHooks.setFloat(LIVING_HEALTH, entity, health);
        }
    }

    private static void setBoolean(Field field, Object target, boolean value) {
        if (field != null) {
            try {
                field.setBoolean(target, value);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void setInt(Field field, Object target, int value) {
        if (field != null) {
            try {
                field.setInt(target, value);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void setFloat(Field field, Object target, float value) {
        if (field != null) {
            try {
                field.setFloat(target, value);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private static void setField(Field field, Object target, Object value) {
        if (field != null) {
            try {
                field.set(target, value);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

