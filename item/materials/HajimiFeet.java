/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.materials;

import com.manbo.v2c.util.ColorUtils;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HajimiFeet
extends Item {
    public HajimiFeet() {
        super(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.EPIC));
    }

    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (!level.isClientSide()) {
            stack.enchant(Enchantments.SHARPNESS, 100);
            stack.enchant(Enchantments.MOB_LOOTING, 81);
        }
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public Component getName(ItemStack stack) {
        return ColorUtils.createWhiteBlueGradientText(Component.translatable((String)this.getDescriptionId()).getString());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        String[] lines;
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String)"tooltip.manbov2c.hajimi_feet").getString();
        for (String line : lines = text.split("\n")) {
            tooltipComponents.add((Component)ColorUtils.createWhiteBlueGradientText(line));
        }
    }
}

