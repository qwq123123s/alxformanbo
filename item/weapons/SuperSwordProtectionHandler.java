/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingTickEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class SuperSwordProtectionHandler {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!SuperSwordProtectionHandler.hasSword(player)) {
            return;
        }
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!SuperSwordProtectionHandler.hasSword(player)) {
            return;
        }
        player.setHealth(player.getMaxHealth());
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!SuperSwordProtectionHandler.hasSword(player)) {
            return;
        }
        player.getActiveEffects().removeIf(e -> !e.getEffect().isBeneficial());
        player.setHealth(player.getMaxHealth());
    }

    private static boolean hasSword(Player player) {
        if (player.getMainHandItem().is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) {
            return true;
        }
        if (player.getOffhandItem().is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) {
            return true;
        }
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) continue;
            return true;
        }
        return false;
    }
}

