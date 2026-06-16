/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.PickaxeItem
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.util.ColorUtils;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ManboPickaxeSuper
        extends PickaxeItem {
    public ManboPickaxeSuper() {
        super((Tier) Tiers.NETHERITE, 1, -2.8f,
                new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity instanceof Player) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt("HideFlags", nbt.getInt("HideFlags") | 3);
            if (!stack.isEnchanted()) {
                stack.enchant(Enchantments.BLOCK_FORTUNE, 16);
            }
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String) "item.manbov2c.manbo_pickaxe_super").getString();
        return ColorUtils.createYellowCyanGradientText(baseName, 5.0f);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
            TooltipFlag isAdvanced) {
        String[] lines;
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String) "tooltip.manbov2c.manbo_pickaxe_super").getString();
        for (String line : lines = text.split("\n")) {
            MutableComponent coloredText = ColorUtils.createYellowCyanGradientText(line, 6.0f);
            tooltipComponents.add((Component) coloredText);
        }
        Map enchants = EnchantmentHelper.getEnchantments((ItemStack) stack);
        for (Object entryObj : enchants.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            String name = Component.translatable((String) ((Enchantment) entry.getKey()).getDescriptionId())
                    .getString();
            String display = name + " " + ColorUtils.toRoman((Integer) entry.getValue());
            tooltipComponents.add((Component) ColorUtils.createYellowCyanGradientText(display, 8.0f));
        }
        tooltipComponents.add((Component) Component.literal((String) ""));
        tooltipComponents.add((Component) ColorUtils
                .createYellowCyanGradientText("\u865a\u65e0\u661f\u7a7a \u6316\u6398\u901f\u5ea6", 4.0f));
        tooltipComponents.add((Component) ColorUtils
                .createYellowCyanGradientText("\u865a\u65e0\u661f\u7a7a \u6316\u6398\u6548\u7387", 4.0f));
    }
}
