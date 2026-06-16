/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Post
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.client;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT})
public class KingsShadowOverlayRenderer {
    private static long defenseMessageEndTime = 0L;
    private static final long DEFENSE_DURATION_MS = 2000L;
    private static long equipMessageEndTime = 0L;
    private static final long EQUIP_DURATION_MS = 3000L;

    public static void onPacketReceived(int type) {
        if (type == 0) {
            defenseMessageEndTime = System.currentTimeMillis() + 2000L;
        } else if (type == 1) {
            equipMessageEndTime = System.currentTimeMillis() + 3000L;
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        String text;
        float progress;
        int alpha;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.font == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now < defenseMessageEndTime && (alpha = KingsShadowOverlayRenderer.getFadeAlpha(progress = 1.0f - (float)(defenseMessageEndTime - now) / 2000.0f)) > 0) {
            text = "\u563f\u563f\uff0c\u6211\u53ef\u662f\u5e1d\u738b\u5594\uff01\u5730\u4f4d\u5f88\u9ad8\u7684\u266a";
            KingsShadowOverlayRenderer.renderPinkGoldGradient(event.getGuiGraphics(), text, mc, alpha);
        }
        if (now < equipMessageEndTime && (alpha = KingsShadowOverlayRenderer.getFadeAlpha(progress = 1.0f - (float)(equipMessageEndTime - now) / 3000.0f)) > 0) {
            text = "\u8fd9\u5c31\u53eb\u6c38\u4e0d\u653e\u5f03\uff01\u4e1c\u6d77\u5e1d\u738b\uff01\uff01";
            KingsShadowOverlayRenderer.renderPinkGoldGradient(event.getGuiGraphics(), text, mc, alpha);
        }
    }

    private static int getFadeAlpha(float progress) {
        if (progress < 0.0f || progress > 1.0f) {
            return 0;
        }
        if (progress < 0.2f) {
            return (int)(255.0f * (progress / 0.2f));
        }
        if (progress > 0.8f) {
            return (int)(255.0f * (1.0f - progress) / 0.2f);
        }
        return 255;
    }

    private static void renderPinkGoldGradient(GuiGraphics graphics, String text, Minecraft mc, int alpha) {
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        Color pink = new Color(255, 105, 180);
        Color gold = new Color(255, 215, 0);
        Color hotPink = new Color(255, 20, 147);
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.2;
        int totalWidth = mc.font.width(text);
        int x = (screenWidth - totalWidth) / 2;
        int y = screenHeight - 50;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (int i = 0; i < text.length(); ++i) {
            float factor;
            Color color2;
            Color color1;
            char ch = text.charAt(i);
            String charStr = String.valueOf(ch);
            int charWidth = mc.font.width(charStr);
            float position = (float)((timeOffset + (double)((float)i * 0.08f)) % 2.0);
            if (position < 1.0f) {
                color1 = pink;
                color2 = gold;
                factor = position;
            } else {
                color1 = gold;
                color2 = hotPink;
                factor = position - 1.0f;
            }
            int r = (int)(((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed())) * (float)alpha / 255.0f);
            int g = (int)(((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen())) * (float)alpha / 255.0f);
            int b = (int)(((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue())) * (float)alpha / 255.0f);
            int color = 0xFF000000 | r << 16 | g << 8 | b;
            int shadowR = (int)((float)r * 0.2f);
            int shadowG = (int)((float)g * 0.2f);
            int shadowB = (int)((float)b * 0.2f);
            int shadowColor = alpha << 24 | shadowR << 16 | shadowG << 8 | shadowB;
            graphics.drawString(mc.font, charStr, x + 1, y + 1, shadowColor, false);
            graphics.drawString(mc.font, charStr, x, y, color, false);
            x += charWidth;
        }
        RenderSystem.disableBlend();
    }
}

