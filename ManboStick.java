/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class ManboStick
extends SwordItem {
    public ManboStick() {
        super((Tier)Tiers.NETHERITE, 2, -2.4f, new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String)"item.manbov2c.manbo_stick").getString();
        return ColorUtils.createRainbowText(baseName, 1.0f, 0.03f);
    }
}

