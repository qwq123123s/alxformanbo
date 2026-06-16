/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.PickaxeItem
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.item.weapons.ManbohajimiSuperSword;
import java.awt.Color;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ManboPickaxeUpgrade
extends PickaxeItem {
    public ManboPickaxeUpgrade() {
        super((Tier)Tiers.NETHERITE, 1, -2.8f, new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity instanceof Player) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt("HideFlags", nbt.getInt("HideFlags") | 3);
            if (!stack.isEnchanted()) {
                stack.enchant(Enchantments.BLOCK_FORTUNE, 128);
            }
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String)"item.manbov2c.manbo_pickaxe_upgrade").getString();
        return ManbohajimiSuperSword.createGreenBlueGradientText(baseName);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        String text = Component.translatable((String)"tooltip.manbov2c.manbo_pickaxe_upgrade").getString();
        String[] lines = text.split("\n");
        Color green = new Color(0, 255, 100);
        Color blue = new Color(0, 100, 255);
        for (String line : lines) {
            tooltip.add(ManbohajimiSuperSword.createDualColorGradient(line, green, blue, 0.8f, 0.04f));
        }
        tooltip.add((Component)Component.literal((String)""));
        tooltip.add(ManbohajimiSuperSword.createGreenBlueGradientText("  \u5947\u8ff9\u5904\u543e\u53ef\u771f\u5bfb \u6316\u6398\u901f\u5ea6"));
        tooltip.add(ManbohajimiSuperSword.createGreenBlueGradientText("  \u5f62\u800c\u4e0a\u4e0d\u77e5\u6240\u8e2a \u6316\u6398\u6548\u7387"));
    }
}

