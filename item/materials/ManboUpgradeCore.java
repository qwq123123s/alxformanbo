/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.materials;

import com.manbo.v2c.util.ColorUtils;
import java.awt.Color;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ManboUpgradeCore
extends Item {
    public ManboUpgradeCore() {
        super(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(64));
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String)"item.manbov2c.manbo_upgrade_core").getString();
        return ColorUtils.createBlueWhiteGradientText(baseName);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String)"tooltip.manbov2c.manbo_upgrade_core").getString();
        String[] lines = text.split("\n");
        Color green = new Color(0, 255, 100);
        Color blue = new Color(0, 100, 255);
        for (String line : lines) {
            tooltipComponents.add((Component)ColorUtils.createDualColorGradient(line, green, blue, 0.8f, 0.04f));
        }
    }
}

