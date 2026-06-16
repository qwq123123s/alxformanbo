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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
public class ManboInfinityCoreOverlayHandler {
    private static boolean tooltipActive = false;
    private static long lastTooltipTime = 0L;
    private static final long TOOLTIP_TIMEOUT_MS = 2000L;
    private static final List<RedHeartParticle> hearts = new ArrayList<RedHeartParticle>();
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        if (event.getItemStack().is((Item)ModItems.MANBO_INFINITY_CORE.get())) {
            tooltipActive = true;
            lastTooltipTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (tooltipActive && System.currentTimeMillis() - lastTooltipTime > 2000L) {
            tooltipActive = false;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            hearts.clear();
            return;
        }
        if (tooltipActive) {
            int count = 1 + RANDOM.nextInt(3);
            for (int i = 0; i < count; ++i) {
                hearts.add(new RedHeartParticle(mc.getWindow().getGuiScaledWidth()));
            }
        }
        float screenHeight = mc.getWindow().getGuiScaledHeight();
        Iterator<RedHeartParticle> iter = hearts.iterator();
        while (iter.hasNext()) {
            RedHeartParticle h = iter.next();
            h.y += h.speedY;
            h.x += Math.sin((double)System.currentTimeMillis() * h.swaySpeed + h.swayOffset) * 0.5;
            h.alpha -= 2;
            if (!(h.y > (double)(screenHeight + 30.0f)) && h.alpha > 0) continue;
            iter.remove();
        }
        if (hearts.size() > 120) {
            hearts.subList(0, hearts.size() - 120).clear();
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        if (!tooltipActive && hearts.isEmpty()) {
            return;
        }
        GuiGraphics gui = event.getGuiGraphics();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = gui.pose().last().pose();
        for (RedHeartParticle h : hearts) {
            if (h.alpha <= 0) continue;
            int a = Math.min(h.alpha, 255);
            ManboInfinityCoreOverlayHandler.drawHeartArc(buffer, matrix, h.x, h.y, h.size, a);
        }
        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
    }

    private static void drawHeartArc(BufferBuilder buffer, Matrix4f matrix, double cx, double cy, float scale, int alpha) {
        int segments = 24;
        float[][] points = new float[segments + 1][2];
        for (int i = 0; i <= segments; ++i) {
            double t = Math.PI * 2 * (double)i / (double)segments;
            float x = (float)(16.0 * Math.pow(Math.sin(t), 3.0));
            float y = (float)(13.0 * Math.cos(t) - 5.0 * Math.cos(2.0 * t) - 2.0 * Math.cos(3.0 * t) - Math.cos(4.0 * t));
            points[i][0] = (float)cx + x * scale / 16.0f;
            points[i][1] = (float)cy - y * scale / 16.0f;
        }
        float centerX = (float)cx;
        float centerY = (float)cy - scale * 0.5f;
        for (int i = 0; i < segments; ++i) {
            buffer.vertex(matrix, centerX, centerY, 0.0f).color(255, 50, 50, alpha).endVertex();
            buffer.vertex(matrix, points[i][0], points[i][1], 0.0f).color(255, 50, 50, alpha).endVertex();
            buffer.vertex(matrix, points[i + 1][0], points[i + 1][1], 0.0f).color(255, 50, 50, alpha).endVertex();
        }
    }

    private static class RedHeartParticle {
        double x;
        double y;
        double speedY;
        double swayOffset;
        double swaySpeed;
        float size;
        int alpha;

        RedHeartParticle(double screenWidth) {
            this.x = RANDOM.nextDouble() * screenWidth;
            this.y = -20.0 - RANDOM.nextDouble() * 30.0;
            this.speedY = 1.5 + RANDOM.nextDouble() * 1.5;
            this.swayOffset = RANDOM.nextDouble() * Math.PI * 2.0;
            this.swaySpeed = 0.02 + RANDOM.nextDouble() * 0.02;
            this.size = 10.0f + RANDOM.nextFloat() * 10.0f;
            this.alpha = 180 + RANDOM.nextInt(75);
        }
    }
}

