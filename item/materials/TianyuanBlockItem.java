/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.Block
 */
package com.manbo.v2c.item.materials;

import com.manbo.v2c.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TianyuanBlockItem
extends BlockItem {
    public TianyuanBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public Component getName(ItemStack stack) {
        return ColorUtils.createWhiteBlueGradientText(Component.translatable((String)this.getDescriptionId()).getString());
    }
}

