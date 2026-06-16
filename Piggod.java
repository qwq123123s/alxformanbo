/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.util.ColorUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class Piggod
extends SwordItem {
    public Piggod() {
        super((Tier)Tiers.NETHERITE, 3, -2.4f, new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity instanceof Player && !stack.isEnchanted()) {
            stack.enchant(Enchantments.FIRE_ASPECT, 1);
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String)"item.manbov2c.piggod").getString();
        return ColorUtils.createGreenRedGradientText(baseName);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        String[] lines;
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String)"tooltip.manbov2c.piggod").getString();
        for (String line : lines = text.split("\n")) {
            tooltipComponents.add((Component)ColorUtils.createGreenRedGradientText(line));
        }
    }
}

