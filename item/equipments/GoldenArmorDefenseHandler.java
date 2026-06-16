/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.equipments;

import com.manbo.v2c.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class GoldenArmorDefenseHandler {
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
        if (!GoldenArmorDefenseHandler.hasAnyGoldenArmor(player)) {
            return;
        }
        event.setAmount(-10.0f);
        DamageSource source = event.getSource();
        Entity attackerEntity = source.getEntity();
        if (attackerEntity instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity)attackerEntity;
            float reflectedDamage = Math.abs(-10.0f) * 10.0f;
            attacker.hurt(attacker.damageSources().magic(), reflectedDamage);
        }
    }

    private static boolean hasAnyGoldenArmor(Player player) {
        return GoldenArmorDefenseHandler.isGoldenArmor(player.getItemBySlot(EquipmentSlot.HEAD)) || GoldenArmorDefenseHandler.isGoldenArmor(player.getItemBySlot(EquipmentSlot.CHEST)) || GoldenArmorDefenseHandler.isGoldenArmor(player.getItemBySlot(EquipmentSlot.LEGS)) || GoldenArmorDefenseHandler.isGoldenArmor(player.getItemBySlot(EquipmentSlot.FEET));
    }

    private static boolean isGoldenArmor(ItemStack stack) {
        return stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_HELMET.get()) || stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_CHESTPLATE.get()) || stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_LEGGINGS.get()) || stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_BOOTS.get());
    }
}

