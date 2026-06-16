/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.world.item.Item
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderTooltipEvent$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
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
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class TianyuanCoreOverlayHandler {
    private static boolean tooltipActive = false;
    private static long lastTooltipTime = 0L;
    private static final long TOOLTIP_TIMEOUT_MS = 200L;
    private static float rotationAngle = 0.0f;
    private static float lastMouseX = 0.0f;
    private static float lastMouseY = 0.0f;

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        if (event.getItemStack().is((Item)ModItems.DATIAN_CORE_JUEX.get())) {
            tooltipActive = true;
            lastTooltipTime = System.currentTimeMillis();
            Minecraft mc = Minecraft.getInstance();
            double mouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth();
            double mouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight();
            lastMouseX = (float)mouseX;
            lastMouseY = (float)mouseY;
        }
    }

    private static void drawHollowStarTriangles(BufferBuilder buffer, Matrix4f matrix, float cx, float cy, float outerR, float innerR, float rotation, int r, int g, int b, int a, float z, float lineWidth) {
        float[][] pts = new float[10][2];
        for (int i = 0; i < 10; ++i) {
            boolean isOuter = i % 2 == 0;
            int idx = i / 2;
            double angle = (double)rotation + (isOuter ? (double)idx : (double)idx + 0.5) * 2.0 * Math.PI / 5.0 - 1.5707963267948966;
            float radius = isOuter ? outerR : innerR;
            pts[i][0] = cx + radius * (float)Math.cos(angle);
            pts[i][1] = cy + radius * (float)Math.sin(angle);
        }
        float hw = lineWidth / 2.0f;
        for (int i = 0; i < 10; ++i) {
            int next = (i + 1) % 10;
            float x2 = pts[next][0];
            float x1 = pts[i][0];
            float dx = x2 - x1;
            float y2 = pts[next][1];
            float y1 = pts[i][1];
            float dy = y2 - y1;
            float len = (float)Math.sqrt(dx * dx + dy * dy);
            if (len < 0.001f) continue;
            float px = -(dy /= len) * hw;
            float py = (dx /= len) * hw;
            float v1x = x1 - px;
            float v1y = y1 - py;
            float v2x = x1 + px;
            float v2y = y1 + py;
            float v3x = x2 - px;
            float v3y = y2 - py;
            float v4x = x2 + px;
            float v4y = y2 + py;
            buffer.vertex(matrix, v1x, v1y, z).color(r, g, b, a).endVertex();
            buffer.vertex(matrix, v2x, v2y, z).color(r, g, b, a).endVertex();
            buffer.vertex(matrix, v3x, v3y, z).color(r, g, b, a).endVertex();
            buffer.vertex(matrix, v2x, v2y, z).color(r, g, b, a).endVertex();
            buffer.vertex(matrix, v4x, v4y, z).color(r, g, b, a).endVertex();
            buffer.vertex(matrix, v3x, v3y, z).color(r, g, b, a).endVertex();
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        if (!tooltipActive) {
            return;
        }
        GuiGraphics gui = event.getGuiGraphics();
        Matrix4f matrix = gui.pose().last().pose();
        float outerRadius = 40.0f;
        float innerRadius = 16.0f;
        double breath = (Math.sin((double)System.currentTimeMillis() / 1000.0 * 2.0) + 1.0) / 2.0;
        int alphaMain = (int)(200.0 + breath * 55.0);
        int alphaGlow = (int)(80.0 + breath * 40.0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        TianyuanCoreOverlayHandler.drawHollowStarTriangles(buffer, matrix, lastMouseX, lastMouseY, outerRadius, innerRadius, rotationAngle, 255, 255, 255, alphaMain, 0.0f, 3.0f);
        tesselator.end();
        float glowOuter = outerRadius * 1.5f;
        float glowInner = innerRadius * 1.5f;
        float glowRot = -rotationAngle * 0.6f;
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        TianyuanCoreOverlayHandler.drawHollowStarTriangles(buffer, matrix, lastMouseX, lastMouseY, glowOuter, glowInner, glowRot, 255, 255, 255, alphaGlow, 0.0f, 3.0f);
        tesselator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if ((double)(rotationAngle += 0.05f) > Math.PI * 2) {
            rotationAngle = (float)((double)rotationAngle - Math.PI * 2);
        }
        if (tooltipActive && System.currentTimeMillis() - lastTooltipTime > 200L) {
            tooltipActive = false;
        }
    }
}

