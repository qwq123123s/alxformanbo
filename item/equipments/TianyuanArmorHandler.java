/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingTickEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.equipments;

import com.manbo.v2c.ModItems;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class TianyuanArmorHandler {
    private static final Set<MobEffect> BAD_EFFECTS = new HashSet<MobEffect>();

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!TianyuanArmorHandler.hasFullTianyuanArmor(player)) {
            return;
        }
        player.getActiveEffects().removeIf(effect -> BAD_EFFECTS.contains(effect.getEffect()));
    }

    public static boolean hasFullTianyuanArmor(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        return head.is((Item)ModItems.TIANYUAN_HELMET.get()) && chest.is((Item)ModItems.TIANYUAN_CHESTPLATE.get()) && legs.is((Item)ModItems.TIANYUAN_LEGGINGS.get()) && feet.is((Item)ModItems.TIANYUAN_BOOTS.get());
    }

    static {
        BAD_EFFECTS.add(MobEffects.POISON);
        BAD_EFFECTS.add(MobEffects.WITHER);
        BAD_EFFECTS.add(MobEffects.BLINDNESS);
        BAD_EFFECTS.add(MobEffects.DARKNESS);
        BAD_EFFECTS.add(MobEffects.HUNGER);
        BAD_EFFECTS.add(MobEffects.WEAKNESS);
        BAD_EFFECTS.add(MobEffects.MOVEMENT_SLOWDOWN);
        BAD_EFFECTS.add(MobEffects.DIG_SLOWDOWN);
        BAD_EFFECTS.add(MobEffects.CONFUSION);
        BAD_EFFECTS.add(MobEffects.UNLUCK);
        BAD_EFFECTS.add(MobEffects.LEVITATION);
        BAD_EFFECTS.add(MobEffects.GLOWING);
    }
}

