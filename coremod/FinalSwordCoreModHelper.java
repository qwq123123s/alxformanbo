/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package com.manbo.v2c.coremod;

import com.manbo.v2c.ModItems;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FinalSwordCoreModHelper {
    private static final String RECURSION_GUARD = "manbov2c_final_kill_guard";

    public static boolean tryKillEntity(LivingEntity entity, DamageSource source) {
        Player attacker;
        ItemStack weapon;
        Entity entity2;
        if (entity == null || entity.level().isClientSide()) {
            return false;
        }
        if (entity.getPersistentData().getBoolean(RECURSION_GUARD)) {
            return false;
        }
        boolean shouldKill = false;
        if (source != null && (entity2 = source.getDirectEntity()) instanceof Player
                && !(weapon = (attacker = (Player) entity2).getMainHandItem()).isEmpty()
                && weapon.is((Item) ModItems.MANBO_FINAL_SWORD.get())) {
            shouldKill = true;
        }
        if (!shouldKill && entity.getPersistentData().getBoolean("manbov2c_final_bypasshurt")) {
            shouldKill = true;
            entity.getPersistentData().remove("manbov2c_final_bypasshurt");
        }
        if (shouldKill) {
            entity.getPersistentData().putBoolean(RECURSION_GUARD, true);
            if (source != null) {
                Entity sourceEntity = source.getEntity();
                if (sourceEntity instanceof Player) {
                    attacker = (Player) sourceEntity;
                    try {
                        Class<?> handlerClass = Class.forName("com.manbo.v2c.item.weapons.ManboFinalSwordEventHandler");
                        Method onKill = handlerClass.getDeclaredMethod("onCoremodKill", LivingEntity.class,
                                Player.class);
                        onKill.invoke(null, entity, attacker);
                    } catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
            try {
                entity.kill();
            } catch (Exception attacker2) {
                // empty catch block
            }
            try {
                entity.remove(Entity.RemovalReason.KILLED);
            } catch (Exception attacker2) {
                // empty catch block
            }
            try {
                Method dieMethod = LivingEntity.class.getDeclaredMethod("die", DamageSource.class);
                dieMethod.setAccessible(true);
                dieMethod.invoke(entity, entity.damageSources().genericKill());
            } catch (Exception dieMethod) {
                // empty catch block
            }
            try {
                Field healthField = LivingEntity.class.getDeclaredField("health");
                healthField.setAccessible(true);
                healthField.setFloat(entity, -3.4028235E38f);
            } catch (Exception exception) {
                // empty catch block
            }
            entity.setPos(Double.NaN, Double.NaN, Double.NaN);
            try {
                entity.discard();
            } catch (Exception exception) {
                // empty catch block
            }
            return true;
        }
        return false;
    }
}
