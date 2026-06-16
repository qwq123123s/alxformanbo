/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.registries.ForgeRegistries
 */
package com.manbo.v2c.coremod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class UnknownArmorHelper {
    public static boolean shouldCancelDamage(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof Player)) {
            return false;
        }
        Player player = (Player) entity;
        if (player.level().isClientSide()) {
            return false;
        }
        return UnknownArmorHelper.hasAnyUnknownArmor(player);
    }

    private static boolean hasAnyUnknownArmor(Player player) {
        if (UnknownArmorHelper.isUnknownArmor(player.getItemBySlot(EquipmentSlot.HEAD))) {
            return true;
        }
        if (UnknownArmorHelper.isUnknownArmor(player.getItemBySlot(EquipmentSlot.CHEST))) {
            return true;
        }
        if (UnknownArmorHelper.isUnknownArmor(player.getItemBySlot(EquipmentSlot.LEGS))) {
            return true;
        }
        if (UnknownArmorHelper.isUnknownArmor(player.getItemBySlot(EquipmentSlot.FEET))) {
            return true;
        }
        return UnknownArmorHelper.isKingsShadow(player.getItemBySlot(EquipmentSlot.CHEST));
    }

    private static boolean isUnknownArmor(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (registryName == null) {
            return false;
        }
        if (!"manbov2c".equals(registryName.getNamespace())) {
            return false;
        }
        String path = registryName.getPath();
        return "manbo_infinity_unknown_helmet".equals(path) || "manbo_infinity_unknown_chestplate".equals(path)
                || "manbo_infinity_unknown_leggings".equals(path) || "manbo_infinity_unknown_boots".equals(path);
    }

    private static boolean isKingsShadow(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (registryName == null) {
            return false;
        }
        if (!"manbov2c".equals(registryName.getNamespace())) {
            return false;
        }
        return "kings_shadow".equals(registryName.getPath());
    }
}
