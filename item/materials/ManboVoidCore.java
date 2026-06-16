/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.materials;

import com.manbo.v2c.client.VoidCoreOverlayHandler;
import com.manbo.v2c.util.ColorUtils;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ManboVoidCore
extends Item {
    private static final Random RANDOM = new Random();

    public ManboVoidCore() {
        super(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(64));
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String)"item.manbov2c.manbo_void_core").getString();
        return ColorUtils.createYellowGreenPurpleGradientText(baseName);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add((Component)this.createGarbledLine(VoidCoreOverlayHandler.getGarbledPrefix()));
        String text = Component.translatable((String)"tooltip.manbov2c.manbo_void_core").getString();
        String[] lines = text.split("\n");
        Color red = new Color(255, 0, 0);
        Color purple = new Color(180, 0, 255);
        for (String line : lines) {
            tooltipComponents.add((Component)ColorUtils.createDualColorGradient(line, red, purple, 0.8f, 0.04f));
        }
        tooltipComponents.add((Component)this.createGarbledLine(VoidCoreOverlayHandler.getGarbledSuffix()));
    }

    private MutableComponent createGarbledLine(String text) {
        MutableComponent component = Component.literal((String)"");
        for (int i = 0; i < text.length(); ++i) {
            int gray;
            char c = text.charAt(i);
            int n = gray = RANDOM.nextBoolean() ? 170 : 255;
            if (RANDOM.nextInt(3) == 0) {
                gray = 51;
            }
            int color = 0xFF000000 | gray << 16 | gray << 8 | gray;
            component.append((Component)Component.literal((String)String.valueOf(c)).setStyle(Style.EMPTY.withColor(color)));
        }
        return component;
    }
}

