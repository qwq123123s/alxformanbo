/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderTooltipEvent$Color
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.util.ColorUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c", bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
public class TooltipColorHandler {
    @SubscribeEvent
    public static void onRenderTooltipColor(RenderTooltipEvent.Color event) {
        if (event.getItemStack().is((Item)ModItems.MANBO_BASIC_SWORD.get()) || event.getItemStack().is((Item)ModItems.MANBO_CORE.get()) || event.getItemStack().is((Item)ModItems.HAJIMI_CORE.get())) {
            int color1 = ColorUtils.getDynamicRainbowColor(0.0f, 0.05f);
            int color2 = ColorUtils.getDynamicRainbowColor(0.8f, 0.05f);
            int bgColor1 = color1 & 0xFFFFFF | Integer.MIN_VALUE;
            int bgColor2 = color2 & 0xFFFFFF | Integer.MIN_VALUE;
            int borderColor = -16711681;
            event.setBackgroundStart(bgColor1);
            event.setBackgroundEnd(bgColor2);
            event.setBorderStart(borderColor);
            event.setBorderEnd(borderColor);
        }
    }
}

