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
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraftforge.api.distmarker.Dist
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
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ManboVoidScreenStaticEffect {
    private static final Random RANDOM = new Random();
    private static boolean activeEffect = false;

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
            if (!slot.hasItem() || !slot.getItem().is((Item)ModItems.MANBO_VOID_SWORD.get())) continue;
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
        ManboVoidScreenStaticEffect.renderStaticNoise(event.getGuiGraphics().pose(), width, height);
    }

    private static void renderStaticNoise(PoseStack poseStack, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        int cellSize = 3;
        float alpha = 0.25f;
        Matrix4f matrix = poseStack.last().pose();
        for (int x = 0; x < width; x += cellSize) {
            for (int y = 0; y < height; y += cellSize) {
                int gray;
                int r = gray = RANDOM.nextInt(256);
                int g = gray;
                int b = gray;
                if (RANDOM.nextInt(10) == 0) {
                    r = 100 + RANDOM.nextInt(100);
                    g = RANDOM.nextInt(50);
                    b = 150 + RANDOM.nextInt(105);
                }
                float x0 = x;
                float y0 = y;
                float x1 = Math.min(x + cellSize, width);
                float y1 = Math.min(y + cellSize, height);
                buffer.vertex(matrix, x0, y1, 0.0f).color(r, g, b, (int)(alpha * 255.0f)).endVertex();
                buffer.vertex(matrix, x1, y1, 0.0f).color(r, g, b, (int)(alpha * 255.0f)).endVertex();
                buffer.vertex(matrix, x1, y0, 0.0f).color(r, g, b, (int)(alpha * 255.0f)).endVertex();
                buffer.vertex(matrix, x0, y0, 0.0f).color(r, g, b, (int)(alpha * 255.0f)).endVertex();
            }
        }
        tesselator.end();
        RenderSystem.disableBlend();
    }
}

