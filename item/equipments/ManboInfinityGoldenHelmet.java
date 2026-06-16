/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.equipments;

import com.manbo.v2c.item.equipments.InfinityArmorMaterial;
import com.manbo.v2c.util.ColorUtils;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ManboInfinityGoldenHelmet
extends ArmorItem {
    public ManboInfinityGoldenHelmet() {
        super(InfinityArmorMaterial.INFINITY, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().rarity(Rarity.EPIC));
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (!stack.isEnchanted()) {
                stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 35);
                stack.enchant(Enchantments.FIRE_PROTECTION, 35);
                stack.enchant(Enchantments.BLAST_PROTECTION, 35);
                stack.enchant(Enchantments.PROJECTILE_PROTECTION, 35);
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD) == stack) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 2, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, true));
            }
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        String[] lines;
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String)"tooltip.manbov2c.manbo_infinity_golden_helmet").getString();
        for (String line : lines = text.split("\n")) {
            tooltipComponents.add((Component)ColorUtils.createGoldShinyGradientText(line));
        }
    }
}

