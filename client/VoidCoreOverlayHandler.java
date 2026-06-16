/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.world.item.Item
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderTooltipEvent$Pre
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class VoidCoreOverlayHandler {
    private static final Random RANDOM = new Random();
    private static final int[] GARBLED_CODEPOINTS = new int[]{23553, 34849, 39174, 101, 104, 47, 34, 88, 102, 84, 40, 20481, 38001, 27, 30478, 38173, 39, 63, 115, 102, 31839, 52, 25, 107, 47, 119, 22366, 23639, 47, 60, 88, 26712, 35772, 1648, 48, 83, 37035, 63, 63, 43, 40096, 21773, 24668, 33581, 121, 123, 35269, 31789, 32182, 25530, 84, 26843, 22804, 60, 63, 79, 24, 28392, 25020, 28711, 31798, 27450, 34615, 23930, 63, 15, 28996, 22386, 16, 25300, 117, 79, 38514, 38117, 35033, 38845, 21820, 34608, 113, 40084, 73, 259, 19, 31060, 28758, 94, 77, 35, 63, 63, 104, 4, 4, 2, 26, 1, 23895, 40199, 8364, 68, 37718, 36, 100, 62, 38682, 34776, 22982, 72, 29130, 26684, 34515, 23001, 63, 59, 22634, 28460, 35952, 24620, 63, 35586, 24620, 44, 50, 67, 99, 20339, 63, 88, 27709, 24373, 47, 22979, 89, 26033, 29539, 63, 38750, 23817, 63, 24167, 66, 26, 63, 63, 40071, 63, 36, 26796, 99, 34775, 20723, 37, 88, 30897, 55, 112};
    private static String garbledPrefix = VoidCoreOverlayHandler.generateGarbledText(24);
    private static String garbledSuffix = VoidCoreOverlayHandler.generateGarbledText(24);
    private static boolean tooltipActive = false;
    private static int tooltipTimeout = 0;
    private static int squareX1 = 0;
    private static int squareY1 = 0;
    private static int squareTimer1 = 0;
    private static int squareX2 = 0;
    private static int squareY2 = 0;
    private static int squareTimer2 = 0;
    private static int squareX3 = 0;
    private static int squareY3 = 0;
    private static int squareTimer3 = 0;
    private static final int SQUARE_SIZE = 6;
    private static final int SQUARE_DURATION = 2;

    private static char charFromCodePoints() {
        int cp = GARBLED_CODEPOINTS[RANDOM.nextInt(GARBLED_CODEPOINTS.length)];
        if (cp <= 65535) {
            return (char)cp;
        }
        return (char)(57344 + RANDOM.nextInt(4096));
    }

    public static String getGarbledPrefix() {
        return garbledPrefix;
    }

    public static String getGarbledSuffix() {
        return garbledSuffix;
    }

    private static String generateGarbledText(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(VoidCoreOverlayHandler.charFromCodePoints());
        }
        return sb.toString();
    }

    private static void spawnSquare() {
        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        int x = RANDOM.nextInt(screenW - 6);
        int y = RANDOM.nextInt(screenH - 6);
        if (squareTimer1 <= 0) {
            squareX1 = x;
            squareY1 = y;
            squareTimer1 = 2;
        } else if (squareTimer2 <= 0) {
            squareX2 = x;
            squareY2 = y;
            squareTimer2 = 2;
        } else if (squareTimer3 <= 0) {
            squareX3 = x;
            squareY3 = y;
            squareTimer3 = 2;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        garbledPrefix = VoidCoreOverlayHandler.generateGarbledText(24);
        garbledSuffix = VoidCoreOverlayHandler.generateGarbledText(24);
        if (tooltipActive) {
            if (squareTimer1 > 0) {
                --squareTimer1;
            }
            if (squareTimer2 > 0) {
                --squareTimer2;
            }
            if (squareTimer3 > 0) {
                --squareTimer3;
            }
            if (RANDOM.nextInt(3) == 0) {
                VoidCoreOverlayHandler.spawnSquare();
            }
        }
        if (tooltipTimeout > 0 && --tooltipTimeout <= 0) {
            tooltipActive = false;
        }
    }

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        if (event.getItemStack().is((Item)ModItems.MANBO_VOID_CORE.get())) {
            tooltipActive = true;
            tooltipTimeout = 5;
            if (squareTimer1 <= 0 && squareTimer2 <= 0 && squareTimer3 <= 0) {
                VoidCoreOverlayHandler.spawnSquare();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!tooltipActive) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen == null) {
            return;
        }
        GuiGraphics graphics = event.getGuiGraphics();
        if (squareTimer1 > 0) {
            graphics.fill(squareX1, squareY1, squareX1 + 6, squareY1 + 6, -16777216);
        }
        if (squareTimer2 > 0) {
            graphics.fill(squareX2, squareY2, squareX2 + 6, squareY2 + 6, -16777216);
        }
        if (squareTimer3 > 0) {
            graphics.fill(squareX3, squareY3, squareX3 + 6, squareY3 + 6, -16777216);
        }
    }
}

