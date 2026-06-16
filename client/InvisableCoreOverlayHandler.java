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
public class InvisableCoreOverlayHandler {
    private static final Random RANDOM = new Random();
    private static final int[] GARBLED_CODEPOINTS = new int[]{23553, 34849, 39174, 101, 104, 47, 34, 88, 102, 84, 40, 20481, 38001, 27, 30478, 38173, 39, 63, 115, 102, 31839, 52, 25, 107, 47, 119, 22366, 23639, 47, 60, 88, 26712, 35772, 1648, 48, 83, 37035, 63, 63, 43, 40096, 21773, 24668, 33581, 121, 123, 35269, 31789, 32182, 25530, 84, 26843, 22804, 60, 63, 79, 24, 28392, 25020, 28711, 31798, 27450, 34615, 23930, 63, 15, 28996, 22386, 16, 25300, 117, 79, 38514, 38117, 35033, 38845, 21820, 34608, 113, 40084, 73, 259, 19, 31060, 28758, 94, 77, 35, 63, 63, 104, 4, 4, 2, 26, 1, 23895, 40199, 8364, 68, 37718, 36, 100, 62, 38682, 34776, 22982, 72, 29130, 26684, 34515, 23001, 63, 59, 22634, 28460, 35952, 24620, 63, 35586, 24620, 44, 50, 67, 99, 20339, 63, 88, 27709, 24373, 47, 22979, 89, 26033, 29539, 63, 38750, 23817, 63, 24167, 66, 26, 63, 63, 40071, 63, 36, 26796, 99, 34775, 20723, 37, 88, 30897, 55, 112};
    private static String garbledPrefix = InvisableCoreOverlayHandler.generateGarbledText(24);
    private static String garbledSuffix = InvisableCoreOverlayHandler.generateGarbledText(24);
    private static boolean tooltipActive = false;
    private static int tooltipTimeout = 0;
    private static final int MAX_SQUARES = 9;
    private static final int[] squareX = new int[9];
    private static final int[] squareY = new int[9];
    private static final int[] squareTimer = new int[9];
    private static final int[] squareColor = new int[9];
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
            sb.append(InvisableCoreOverlayHandler.charFromCodePoints());
        }
        return sb.toString();
    }

    private static void spawnSquare() {
        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        int x = RANDOM.nextInt(screenW - 6);
        int y = RANDOM.nextInt(screenH - 6);
        int roll = RANDOM.nextInt(10);
        int color = roll < 4 ? -16777216 : (roll < 7 ? -65536 : -65281);
        for (int i = 0; i < 9; ++i) {
            if (squareTimer[i] > 0) continue;
            InvisableCoreOverlayHandler.squareX[i] = x;
            InvisableCoreOverlayHandler.squareY[i] = y;
            InvisableCoreOverlayHandler.squareTimer[i] = 2;
            InvisableCoreOverlayHandler.squareColor[i] = color;
            return;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        garbledPrefix = InvisableCoreOverlayHandler.generateGarbledText(24);
        garbledSuffix = InvisableCoreOverlayHandler.generateGarbledText(24);
        if (tooltipActive) {
            for (int i = 0; i < 9; ++i) {
                if (squareTimer[i] <= 0) continue;
                int n = i;
                squareTimer[n] = squareTimer[n] - 1;
            }
            if (RANDOM.nextInt(2) == 0) {
                InvisableCoreOverlayHandler.spawnSquare();
            }
        }
        if (tooltipTimeout > 0 && --tooltipTimeout <= 0) {
            tooltipActive = false;
        }
    }

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        if (event.getItemStack().is((Item)ModItems.MANBO_INVISABLE_CORE.get())) {
            tooltipActive = true;
            tooltipTimeout = 5;
            boolean allEmpty = true;
            for (int i = 0; i < 9; ++i) {
                if (squareTimer[i] <= 0) continue;
                allEmpty = false;
                break;
            }
            if (allEmpty) {
                InvisableCoreOverlayHandler.spawnSquare();
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
        for (int i = 0; i < 9; ++i) {
            if (squareTimer[i] <= 0) continue;
            graphics.fill(squareX[i], squareY[i], squareX[i] + 6, squareY[i] + 6, squareColor[i]);
        }
    }
}

