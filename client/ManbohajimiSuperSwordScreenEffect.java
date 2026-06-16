/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.DeathScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.ScreenEvent$Opening
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.event.ScreenEvent$Render$Pre
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.joml.Matrix4f
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ManbohajimiSuperSwordScreenEffect {
    private static final Random RANDOM = new Random();
    private static boolean activeEffect = false;
    private static long lastFrameTime = 0L;

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        if (!(event.getScreen() instanceof DeathScreen)) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        boolean hasSword = false;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) continue;
            hasSword = true;
            break;
        }
        if (!hasSword && player.getOffhandItem().is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) {
            hasSword = true;
        }
        if (!hasSword && player.getMainHandItem().is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) {
            hasSword = true;
        }
        if (!hasSword) {
            return;
        }
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof AbstractContainerScreen)) {
            if (activeEffect) {
                activeEffect = false;
            }
            return;
        }
        AbstractContainerScreen containerScreen = (AbstractContainerScreen)screen;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            if (activeEffect) {
                activeEffect = false;
            }
            return;
        }
        double mouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight();
        boolean hovering = false;
        for (Slot slot : containerScreen.getMenu().slots) {
            if (!slot.hasItem() || !slot.getItem().is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) continue;
            float sx = containerScreen.getGuiLeft() + slot.x;
            float sy = containerScreen.getGuiTop() + slot.y;
            if (!(mouseX >= (double)sx) || !(mouseX < (double)(sx + 16.0f)) || !(mouseY >= (double)sy) || !(mouseY < (double)(sy + 16.0f))) continue;
            hovering = true;
            break;
        }
        activeEffect = hovering;
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        if (!activeEffect) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        ManbohajimiSuperSwordScreenEffect.renderColorGlitch(event.getGuiGraphics().pose(), width, height);
    }

    private static void renderColorGlitch(PoseStack poseStack, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        int cellSize = 4;
        float globalAlpha = 0.18f;
        double time = (double)System.currentTimeMillis() / 1000.0;
        Matrix4f matrix = poseStack.last().pose();
        for (int x = 0; x < width; x += cellSize) {
            for (int y = 0; y < height; y += cellSize) {
                float hue = (float)((Math.sin((double)x * 0.01 + time) + Math.sin((double)y * 0.008 + time * 0.7) + Math.sin(time * 0.5)) / 3.0 + 0.5);
                if (RANDOM.nextFloat() < 0.03f) {
                    hue = RANDOM.nextFloat();
                }
                int rgb = Color.HSBtoRGB(hue, 0.9f, 0.7f + RANDOM.nextFloat() * 0.3f);
                int r = rgb >> 16 & 0xFF;
                int g = rgb >> 8 & 0xFF;
                int b = rgb & 0xFF;
                float x0 = x;
                float y0 = y;
                float x1 = x + cellSize;
                float y1 = y + cellSize;
                buffer.vertex(matrix, x0, y0, 0.0f).color(r, g, b, (int)(globalAlpha * 255.0f)).endVertex();
                buffer.vertex(matrix, x1, y0, 0.0f).color(r, g, b, (int)(globalAlpha * 255.0f)).endVertex();
                buffer.vertex(matrix, x1, y1, 0.0f).color(r, g, b, (int)(globalAlpha * 255.0f)).endVertex();
                buffer.vertex(matrix, x0, y1, 0.0f).color(r, g, b, (int)(globalAlpha * 255.0f)).endVertex();
            }
        }
        tesselator.end();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        int bandCount = 8;
        float bandAlpha = 0.06f;
        for (int i = 0; i < bandCount; ++i) {
            float bandY = (float)((Math.sin(time * 0.6 + (double)i * 1.2) * 0.5 + 0.5) * (double)height);
            float bandH = 12.0f + (float)(Math.sin(time * 0.3 + (double)i) * 6.0);
            float hue = (float)(((double)((float)i / (float)bandCount) + time * 0.03) % 1.0);
            int rgb = Color.HSBtoRGB(hue, 0.7f, 0.9f);
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            buffer.vertex(matrix, 0.0f, bandY, 0.0f).color(r, g, b, (int)(bandAlpha * 255.0f)).endVertex();
            buffer.vertex(matrix, (float)width, bandY, 0.0f).color(r, g, b, (int)(bandAlpha * 255.0f)).endVertex();
            buffer.vertex(matrix, (float)width, bandY + bandH, 0.0f).color(r, g, b, (int)(bandAlpha * 255.0f)).endVertex();
            buffer.vertex(matrix, 0.0f, bandY + bandH, 0.0f).color(r, g, b, (int)(bandAlpha * 255.0f)).endVertex();
        }
        tesselator.end();
        RenderSystem.disableBlend();
    }
}

