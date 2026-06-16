/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.equipments;

import com.manbo.v2c.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class HighTierArmorHandler {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!HighTierArmorHandler.hasHighTierArmor(player)) {
            return;
        }
        event.setCanceled(true);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!HighTierArmorHandler.hasHighTierArmor(player)) {
            return;
        }
        event.setCanceled(true);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!HighTierArmorHandler.hasHighTierArmor(player)) {
            return;
        }
        event.setCanceled(true);
        player.setHealth(player.getMaxHealth());
    }

    private static boolean hasHighTierArmor(Player player) {
        return HighTierArmorHandler.isHardenedSet(player) || HighTierArmorHandler.isAlepbSet(player) || HighTierArmorHandler.isUnknownSet(player) || HighTierArmorHandler.isKingsShadowSet(player);
    }

    private static boolean isHardenedSet(Player player) {
        return HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.HEAD), (Item)ModItems.MANBO_INFINITY_UPGRADE_HELMET.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.CHEST), (Item)ModItems.MANBO_INFINITY_UPGRADE_CHESTPLATE.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.LEGS), (Item)ModItems.MANBO_INFINITY_UPGRADE_LEGGINGS.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.FEET), (Item)ModItems.MANBO_INFINITY_UPGRADE_BOOTS.get());
    }

    private static boolean isAlepbSet(Player player) {
        return HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.HEAD), (Item)ModItems.MANBO_INFINITY_ALEPB_HELMET.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.CHEST), (Item)ModItems.MANBO_INFINITY_ALEPB_CHESTPLATE.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.LEGS), (Item)ModItems.MANBO_INFINITY_ALEPB_LEGGINGS.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.FEET), (Item)ModItems.MANBO_INFINITY_ALEPB_BOOTS.get());
    }

    private static boolean isUnknownSet(Player player) {
        return HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.HEAD), (Item)ModItems.MANBO_INFINITY_UNKNOWN_HELMET.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.CHEST), (Item)ModItems.MANBO_INFINITY_UNKNOWN_CHESTPLATE.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.LEGS), (Item)ModItems.MANBO_INFINITY_UNKNOWN_LEGGINGS.get()) || HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.FEET), (Item)ModItems.MANBO_INFINITY_UNKNOWN_BOOTS.get());
    }

    private static boolean isKingsShadowSet(Player player) {
        return HighTierArmorHandler.isItem(player.getItemBySlot(EquipmentSlot.CHEST), (Item)ModItems.KINGS_SHADOW.get());
    }

    private static boolean isItem(ItemStack stack, Item item) {
        return !stack.isEmpty() && stack.is(item);
    }
}

