/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.RenderStateShard$CullStateShard
 *  net.minecraft.client.renderer.RenderStateShard$DepthTestStateShard
 *  net.minecraft.client.renderer.RenderStateShard$ShaderStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TransparencyStateShard
 *  net.minecraft.client.renderer.RenderStateShard$WriteMaskStateShard
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.RenderType$CompositeState
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Pre
 *  net.minecraftforge.client.event.RenderTooltipEvent$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.joml.Matrix4f
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.client.ManboShaders;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(modid = "manbov2c", value = { Dist.CLIENT }, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManboUnknownCoreVisualHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger((String) "ManboUnknownCore");
    private static final long CLOCK_BASE = System.nanoTime();
    private static final Random RANDOM = new Random();
    private static boolean tooltipActive = false;
    private static long lastTooltipTime = 0L;
    private static final long TOOLTIP_TIMEOUT_MS = 2000L;
    private static final List<RainbowHeartParticle> hearts = new ArrayList<RainbowHeartParticle>();
    private static int tooltipX = 0;
    private static int tooltipY = 0;
    private static int tooltipW = 0;
    private static int tooltipH = 0;
    private static final int RAY_COUNT = 9;
    private static final float RAY_LENGTH = 1.15f;
    private static final float RAY_MIN_WIDTH = 0.02f;
    private static final float RAY_MAX_WIDTH = 0.525f;
    private static final float ROTATE_SPEED = 1.0f;
    private static final float PULSE_SPEED = 1.5f;
    private static final float PULSE_AMOUNT = 0.12f;
    private static final float TOOLTIP_SCALE_BOOST = 1.4f;
    private static final int RING_SEGMENTS = 42;
    private static final float RING_INNER_RADIUS = 0.3f;
    private static final float RING_OUTER_RADIUS = 0.6f;
    private static final RenderType GLOW_ADDITIVE = RenderType.create((String) "unknown_core_glow",
            (VertexFormat) DefaultVertexFormat.POSITION_COLOR, (VertexFormat.Mode) VertexFormat.Mode.TRIANGLES,
            (int) 0x200000, (boolean) false, (boolean) true,
            (RenderType.CompositeState) RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard("additive", () -> {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc((GlStateManager.SourceFactor) GlStateManager.SourceFactor.SRC_ALPHA,
                                (GlStateManager.DestFactor) GlStateManager.DestFactor.ONE);
                    }, () -> {
                        RenderSystem.disableBlend();
                        RenderSystem.defaultBlendFunc();
                    })).setCullState(new RenderStateShard.CullStateShard(false))
                    .setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519))
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .createCompositeState(false));

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        if (event.getItemStack().is((Item) ModItems.MANBO_UNKNOWN_CORE.get())) {
            tooltipActive = true;
            lastTooltipTime = System.currentTimeMillis();
            Minecraft mc = Minecraft.getInstance();
            double mouseX = mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth()
                    / (double) mc.getWindow().getScreenWidth();
            double mouseY = mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight()
                    / (double) mc.getWindow().getScreenHeight();
            ManboUnknownCoreVisualHandler.renderRainbowHexagram(event.getGraphics(), (float) mouseX, (float) mouseY,
                    ManboUnknownCoreVisualHandler.getTime());
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
                hearts.add(new RainbowHeartParticle(mc.getWindow().getGuiScaledWidth()));
            }
        }
        float screenHeight = mc.getWindow().getGuiScaledHeight();
        Iterator<RainbowHeartParticle> iter = hearts.iterator();
        while (iter.hasNext()) {
            RainbowHeartParticle h = iter.next();
            h.y += h.speedY;
            h.x += Math.sin((double) System.currentTimeMillis() * h.swaySpeed + h.swayOffset) * 0.5;
            h.alpha -= 2;
            if (!(h.y > (double) (screenHeight + 30.0f)) && h.alpha > 0)
                continue;
            iter.remove();
        }
        if (hearts.size() > 120) {
            hearts.subList(0, hearts.size() - 120).clear();
        }
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        Screen screen;
        GuiGraphics gui = event.getGuiGraphics();
        float time = ManboUnknownCoreVisualHandler.getTime();
        if (!hearts.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f matrix = gui.pose().last().pose();
            for (RainbowHeartParticle h : hearts) {
                if (h.alpha <= 0)
                    continue;
                int a = Math.min(h.alpha, 255);
                float hue = (time * 0.3f + h.hueOffset) % 1.0f;
                int[] rgb = ManboUnknownCoreVisualHandler.hsvToRgb(hue, 1.0f, 1.0f);
                ManboUnknownCoreVisualHandler.drawRainbowHeartArc(buffer, matrix, h.x, h.y, h.size, a, rgb[0], rgb[1],
                        rgb[2]);
            }
            Tesselator.getInstance().end();
            RenderSystem.disableBlend();
        }
        if (!((screen = event.getScreen()) instanceof AbstractContainerScreen)) {
            return;
        }
        AbstractContainerScreen containerScreen = (AbstractContainerScreen) screen;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        double mouseX = mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth()
                / (double) mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight()
                / (double) mc.getWindow().getScreenHeight();
        for (Slot slot : containerScreen.getMenu().slots) {
            if (!slot.hasItem() || !slot.getItem().is((Item) ModItems.MANBO_UNKNOWN_CORE.get()))
                continue;
            float cx = containerScreen.getGuiLeft() + slot.x + 8;
            float cy = containerScreen.getGuiTop() + slot.y + 8;
            boolean hovered = mouseX >= (double) (containerScreen.getGuiLeft() + slot.x)
                    && mouseX < (double) (containerScreen.getGuiLeft() + slot.x + 16)
                    && mouseY >= (double) (containerScreen.getGuiTop() + slot.y)
                    && mouseY < (double) (containerScreen.getGuiTop() + slot.y + 16);
            ManboUnknownCoreVisualHandler.renderRainbowHalo(gui, cx, cy, hovered);
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        float cx = 0.0f;
        float cy = 0.0f;
        boolean found = false;
        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.is((Item) ModItems.MANBO_UNKNOWN_CORE.get())) {
            int slot = mc.player.getInventory().selected;
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            cx = (float) sw / 2.0f - 91.0f + 1.0f + (float) (slot * 20) + 8.0f;
            cy = sh - 22 + 1 + 8;
            found = true;
        }
        if (!found && mc.player.getOffhandItem().is((Item) ModItems.MANBO_UNKNOWN_CORE.get())) {
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            cx = (float) sw / 2.0f + 91.0f + 1.0f + 8.0f;
            cy = sh - 22 + 1 + 8;
            found = true;
        }
        if (!found) {
            return;
        }
        ManboUnknownCoreVisualHandler.renderRainbowHalo(event.getGuiGraphics(), cx, cy, false);
    }

    private static void renderRainbowHexagram(GuiGraphics guiGraphics, float centerX, float centerY, float time) {
        ShaderInstance shader = ManboShaders.getManboHexagramRainbowShader();
        if (shader == null) {
            LOGGER.warn("renderRainbowHexagram: shader is NULL!");
            return;
        }
        float rotation = time * 0.5f;
        float halfSize = 100.0f;
        float lineWidth = Math.max(halfSize * 2.0f * 0.12f, 6.0f);
        float innerSize = Math.max(halfSize - lineWidth, halfSize * 0.3f);
        PoseStack poseStack = new PoseStack();
        poseStack.translate(centerX, centerY, -300.0f);
        poseStack.mulPose(Axis.ZP.rotation(rotation));
        Matrix4f matrix = poseStack.last().pose();
        shader.safeGetUniform("time").set(ManboShaders.getTimeValue());
        shader.safeGetUniform("radius").set(halfSize);
        shader.safeGetUniform("hexCenter").set(centerX, centerY);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(() -> shader);
        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR_TEX);
        float[] upAngles = new float[] { (float) Math.toRadians(-90.0), (float) Math.toRadians(30.0),
                (float) Math.toRadians(150.0) };
        float[] downAngles = new float[] { (float) Math.toRadians(90.0), (float) Math.toRadians(-30.0),
                (float) Math.toRadians(-150.0) };
        float u = 0.5f;
        float v = 0.5f;
        for (int tri = 0; tri < 2; ++tri) {
            float[] angles = tri == 0 ? upAngles : downAngles;
            for (int i = 0; i < 3; ++i) {
                int next = (i + 1) % 3;
                float x1Outer = (float) Math.cos(angles[i]) * halfSize;
                float y1Outer = (float) Math.sin(angles[i]) * halfSize;
                float x2Outer = (float) Math.cos(angles[next]) * halfSize;
                float y2Outer = (float) Math.sin(angles[next]) * halfSize;
                float x1Inner = (float) Math.cos(angles[i]) * innerSize;
                float y1Inner = (float) Math.sin(angles[i]) * innerSize;
                float x2Inner = (float) Math.cos(angles[next]) * innerSize;
                float y2Inner = (float) Math.sin(angles[next]) * innerSize;
                buf.vertex(matrix, x1Outer, y1Outer, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                buf.vertex(matrix, x2Outer, y2Outer, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                buf.vertex(matrix, x2Inner, y2Inner, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                buf.vertex(matrix, x1Outer, y1Outer, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                buf.vertex(matrix, x2Inner, y2Inner, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                buf.vertex(matrix, x1Inner, y1Inner, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
            }
        }
        Tesselator.getInstance().end();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShader(GameRenderer::getPositionShader);
    }

    private static void renderRainbowHalo(GuiGraphics guiGraphics, float cx, float cy, boolean hovered) {
        PoseStack poseStack = guiGraphics.pose();
        float time = ManboUnknownCoreVisualHandler.getTime();
        float pulse = 1.0f + (float) Math.sin((double) (time * 1.5f) * Math.PI * 2.0) * 0.12f;
        float hoverScale = hovered ? 1.4f : 1.0f;
        float totalScale = 21.6f * pulse * hoverScale;
        poseStack.pushPose();
        poseStack.translate(cx, cy, 0.0f);
        poseStack.scale(totalScale, totalScale, 1.0f);
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vc = guiGraphics.bufferSource().getBuffer(GLOW_ADDITIVE);
        ManboUnknownCoreVisualHandler.renderRainbowRing(vc, matrix, time);
        for (int i = 0; i < 9; ++i) {
            float angle = time * 1.0f + (float) ((double) i * 2.0 * Math.PI / 9.0);
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotation(angle));
            float hue = (time * 0.3f + (float) i / 9.0f) % 1.0f;
            int[] rgb = ManboUnknownCoreVisualHandler.hsvToRgb(hue, 1.0f, 1.0f);
            ManboUnknownCoreVisualHandler.renderGlowRay(vc, poseStack.last().pose(), 1.15f, 0.02f, 0.525f, rgb[0],
                    rgb[1], rgb[2], rgb[0] / 3, rgb[1] / 3, rgb[2] / 3);
            poseStack.popPose();
        }
        guiGraphics.bufferSource().endBatch(GLOW_ADDITIVE);
        poseStack.popPose();
    }

    private static void renderGlowRay(VertexConsumer vc, Matrix4f matrix, float length, float minWidth, float maxWidth,
            int r1, int g1, int b1, int r2, int g2, int b2) {
        float x1 = -minWidth / 2.0f;
        float y1 = 0.0f;
        float x2 = minWidth / 2.0f;
        float y2 = 0.0f;
        float x3 = maxWidth / 2.0f;
        float y3 = length;
        float x4 = -maxWidth / 2.0f;
        float y4 = length;
        vc.vertex(matrix, x1, y1, 0.0f).color(r1, g1, b1, 160).endVertex();
        vc.vertex(matrix, x2, y2, 0.0f).color(r1, g1, b1, 160).endVertex();
        vc.vertex(matrix, x3, y3, 0.0f).color(r2, g2, b2, 0).endVertex();
        vc.vertex(matrix, x1, y1, 0.0f).color(r1, g1, b1, 160).endVertex();
        vc.vertex(matrix, x3, y3, 0.0f).color(r2, g2, b2, 0).endVertex();
        vc.vertex(matrix, x4, y4, 0.0f).color(r2, g2, b2, 0).endVertex();
    }

    private static void renderRainbowRing(VertexConsumer vc, Matrix4f matrix, float time) {
        int alpha = 70;
        for (int i = 0; i < 42; ++i) {
            int nextI = (i + 1) % 42;
            float angle1 = (float) (Math.PI * 2 * (double) i / 42.0);
            float angle2 = (float) (Math.PI * 2 * (double) nextI / 42.0);
            float offset1 = ManboUnknownCoreVisualHandler.calculateWaveOffset(time * 4.0f, i);
            float offset2 = ManboUnknownCoreVisualHandler.calculateWaveOffset(time * 4.0f, nextI);
            float innerR1 = 0.3f + offset1;
            float outerR1 = 0.6f + offset1;
            float innerR2 = 0.3f + offset2;
            float outerR2 = 0.6f + offset2;
            float innerX1 = (float) ((double) innerR1 * Math.cos(angle1));
            float innerY1 = (float) ((double) innerR1 * Math.sin(angle1));
            float outerX1 = (float) ((double) outerR1 * Math.cos(angle1));
            float outerY1 = (float) ((double) outerR1 * Math.sin(angle1));
            float innerX2 = (float) ((double) innerR2 * Math.cos(angle2));
            float innerY2 = (float) ((double) innerR2 * Math.sin(angle2));
            float outerX2 = (float) ((double) outerR2 * Math.cos(angle2));
            float outerY2 = (float) ((double) outerR2 * Math.sin(angle2));
            float hue = (time * 0.2f + (float) i / 42.0f) % 1.0f;
            int[] rgb = ManboUnknownCoreVisualHandler.hsvToRgb(hue, 1.0f, 1.0f);
            vc.vertex(matrix, innerX1, innerY1, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            vc.vertex(matrix, outerX1, outerY1, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            vc.vertex(matrix, innerX2, innerY2, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            vc.vertex(matrix, outerX1, outerY1, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            vc.vertex(matrix, outerX2, outerY2, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
            vc.vertex(matrix, innerX2, innerY2, 0.0f).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
        }
    }

    private static float calculateWaveOffset(float time, int index) {
        return (float) (Math.sin(time * 3.5f + (float) index * 1.0f) * (double) 0.17f
                + Math.sin(time * 3.0f + (float) index * 0.5f) * (double) 0.13f
                + Math.cos(time * 3.5f * 0.7f + (float) index * 1.0f * 0.5f) * (double) 0.17f * 0.5);
    }

    private static void drawRainbowHeartArc(BufferBuilder buffer, Matrix4f matrix, double cx, double cy, float scale,
            int alpha, int r, int g, int b) {
        int segments = 24;
        float[][] points = new float[segments + 1][2];
        for (int i = 0; i <= segments; ++i) {
            double t = Math.PI * 2 * (double) i / (double) segments;
            float x = (float) (16.0 * Math.pow(Math.sin(t), 3.0));
            float y = (float) (13.0 * Math.cos(t) - 5.0 * Math.cos(2.0 * t) - 2.0 * Math.cos(3.0 * t)
                    - Math.cos(4.0 * t));
            points[i][0] = (float) cx + x * scale / 16.0f;
            points[i][1] = (float) cy - y * scale / 16.0f;
        }
        float centerX = (float) cx;
        float centerY = (float) cy - scale * 0.5f;
        for (int i = 0; i < segments; ++i) {
            buffer.vertex(matrix, centerX, centerY, 0.0f).color(r, g, b, alpha).endVertex();
            buffer.vertex(matrix, points[i][0], points[i][1], 0.0f).color(r, g, b, alpha).endVertex();
            buffer.vertex(matrix, points[i + 1][0], points[i + 1][1], 0.0f).color(r, g, b, alpha).endVertex();
        }
    }

    private static float getTime() {
        return (float) (System.nanoTime() - CLOCK_BASE) / 1.0E9f;
    }

    private static int[] hsvToRgb(float h, float s, float v) {
        float g = 0;
        float r = 0;
        int i = (int) (h * 6.0f);
        float f = h * 6.0f - (float) i;
        float p = v * (1.0f - s);
        float q = v * (1.0f - f * s);
        float t = v * (1.0f - (1.0f - f) * s);
        return new int[] { (int) (r * 255.0f), (int) (g * 255.0f), (int) ((switch (i % 6) {
            case 0 -> {
                r = v;
                g = t;
                yield p;
            }
            case 1 -> {
                r = q;
                g = v;
                yield p;
            }
            case 2 -> {
                r = p;
                g = v;
                yield t;
            }
            case 3 -> {
                r = p;
                g = q;
                yield v;
            }
            case 4 -> {
                r = t;
                g = p;
                yield v;
            }
            case 5 -> {
                r = v;
                g = p;
                yield q;
            }
            default -> {
                r = 0.0f;
                g = 0.0f;
                yield 0.0f;
            }
        }) * 255.0f) };
    }

    private static class RainbowHeartParticle {
        double x;
        double y;
        double speedY;
        double swayOffset;
        double swaySpeed;
        float size;
        int alpha;
        float hueOffset;

        RainbowHeartParticle(double screenWidth) {
            this.x = RANDOM.nextDouble() * screenWidth;
            this.y = -20.0 - RANDOM.nextDouble() * 30.0;
            this.speedY = 1.5 + RANDOM.nextDouble() * 1.5;
            this.swayOffset = RANDOM.nextDouble() * Math.PI * 2.0;
            this.swaySpeed = 0.02 + RANDOM.nextDouble() * 0.02;
            this.size = 10.0f + RANDOM.nextFloat() * 10.0f;
            this.alpha = 180 + RANDOM.nextInt(75);
            this.hueOffset = RANDOM.nextFloat();
        }
    }
}
