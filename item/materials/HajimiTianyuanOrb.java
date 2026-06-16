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
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HajimiTianyuanOrb
extends Item {
    public HajimiTianyuanOrb() {
        super(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(64));
    }

    public Component getName(ItemStack stack) {
        return ColorUtils.createWhiteBlueGradientText(Component.translatable((String)this.getDescriptionId()).getString());
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        String desc = Component.translatable((String)(this.getDescriptionId() + ".desc")).getString();
        if (!desc.isEmpty() && !desc.equals(this.getDescriptionId() + ".desc")) {
            tooltip.add((Component)ColorUtils.createWhiteBlueGradientText(desc));
        }
    }
}

