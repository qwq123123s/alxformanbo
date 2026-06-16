/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.PoseStack$Pose
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  com.mojang.math.Axis
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.RenderStateShard$CullStateShard
 *  net.minecraft.client.renderer.RenderStateShard$DepthTestStateShard
 *  net.minecraft.client.renderer.RenderStateShard$ShaderStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TransparencyStateShard
 *  net.minecraft.client.renderer.RenderStateShard$WriteMaskStateShard
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.RenderType$CompositeState
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Pre
 *  net.minecraftforge.client.event.RenderPlayerEvent$Post
 *  net.minecraftforge.client.event.RenderTooltipEvent$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.client.ManboRenderTypes;
import com.manbo.v2c.client.ManboShaders;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ManboFinalSwordVisualHandler {
    private static int tooltipX = 0;
    private static int tooltipY = 0;
    private static int tooltipWidth = 0;
    private static int tooltipHeight = 0;
    private static final int RAY_COUNT = 9;
    private static final float RAY_LENGTH = 1.15f;
    private static final float RAY_MIN_WIDTH = 0.02f;
    private static final float RAY_MAX_WIDTH = 0.525f;
    private static final float ROTATE_SPEED = 1.0f;
    private static final float PULSE_SPEED = 1.5f;
    private static final float PULSE_AMOUNT = 0.12f;
    private static final int RING_SEGMENTS = 42;
    private static final float RING_INNER_RADIUS = 0.3f;
    private static final float RING_OUTER_RADIUS = 0.6f;
    private static RenderType glowAdditive;
    private static final long startTime;
    private static boolean wasRenderingFinalSwordTooltip;
    private static final ResourceLocation CHIBI_TANNHAUSER_TEXTURE;
    private static boolean heartTooltipActive;
    private static int heartTooltipTimeout;
    private static final List<OrangeHeartParticle> hearts;
    private static final Random heartRandom;

    private static RenderType getGlowAdditive() {
        if (glowAdditive == null) {
            glowAdditive = RenderType.create((String)"manbo_final_sword_glow", (VertexFormat)DefaultVertexFormat.POSITION_COLOR, (VertexFormat.Mode)VertexFormat.Mode.TRIANGLES, (int)0x200000, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader)).setTransparencyState(new RenderStateShard.TransparencyStateShard("additive", () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
            }, () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            })).setCullState(new RenderStateShard.CullStateShard(false)).setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519)).setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false)).createCompositeState(false));
        }
        return glowAdditive;
    }

    public static void renderHexagramAt(int x, int y, int w, int h) {
        tooltipX = x;
        tooltipY = y;
        tooltipWidth = w;
        tooltipHeight = h;
        ManboFinalSwordVisualHandler.renderHexagramNow();
    }

    private static void renderHexagramNow() {
        ShaderInstance shader = ManboShaders.getManboHexagramPurpleOrangeShader();
        if (shader == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen == null) {
            return;
        }
        float centerX = (float)tooltipX + (float)tooltipWidth / 2.0f;
        float centerY = (float)tooltipY + (float)tooltipHeight / 2.0f;
        float time = ManboShaders.getTimeValue();
        float rotation = time * 0.3f;
        float size = (float)Math.max(tooltipWidth, tooltipHeight) * 1.8f;
        float halfSize = size / 2.0f;
        float lineWidth = Math.max(halfSize * 0.22f, 8.0f);
        float innerSize = Math.max(halfSize - lineWidth, halfSize * 0.3f);
        shader.safeGetUniform("time").set(time);
        shader.safeGetUniform("screenSize").set(ManboShaders.getScreenSize().x, ManboShaders.getScreenSize().y);
        shader.safeGetUniform("yaw").set(0.0f);
        shader.safeGetUniform("pitch").set(0.0f);
        shader.safeGetUniform("radius").set(halfSize);
        shader.safeGetUniform("rotation").set(rotation);
        shader.safeGetUniform("hexCenter").set(centerX, centerY);
        float[] upAngles = new float[]{(float)Math.toRadians(-90.0), (float)Math.toRadians(30.0), (float)Math.toRadians(150.0)};
        float[] downAngles = new float[]{(float)Math.toRadians(90.0), (float)Math.toRadians(-30.0), (float)Math.toRadians(-150.0)};
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer vc = bufferSource.getBuffer(ManboRenderTypes.hexagram_purpleorange);
        ManboFinalSwordVisualHandler.drawThickHexagram(centerX, centerY, halfSize, innerSize, rotation, 180, vc, upAngles, downAngles);
        bufferSource.endBatch(ManboRenderTypes.hexagram_purpleorange);
        float innerScale = 0.92f;
        float innerHalfSize = halfSize * innerScale;
        float innerLineWidth2 = Math.max(innerHalfSize * 0.22f, 6.0f);
        float innerInnerSize = Math.max(innerHalfSize - innerLineWidth2, innerHalfSize * 0.3f);
        shader.safeGetUniform("radius").set(innerHalfSize);
        shader.safeGetUniform("rotation").set(rotation + 0.08f);
        VertexConsumer vc2 = bufferSource.getBuffer(ManboRenderTypes.hexagram_purpleorange);
        ManboFinalSwordVisualHandler.drawThickHexagram(centerX, centerY, innerHalfSize, innerInnerSize, rotation + 0.08f, 220, vc2, upAngles, downAngles);
        bufferSource.endBatch(ManboRenderTypes.hexagram_purpleorange);
        shader.safeGetUniform("radius").set(halfSize);
        shader.safeGetUniform("rotation").set(rotation);
    }

    private static void drawThickHexagram(float centerX, float centerY, float halfSize, float innerSize, float rotation, int layerZ, VertexConsumer vertexConsumer, float[] upAngles, float[] downAngles) {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(centerX, centerY, (float)layerZ);
        poseStack.mulPose(Axis.ZP.rotation(rotation));
        Matrix4f matrix4f = poseStack.last().pose();
        float u = 0.5f;
        float v = 0.5f;
        for (int tri = 0; tri < 2; ++tri) {
            float[] angles = tri == 0 ? upAngles : downAngles;
            for (int i = 0; i < 3; ++i) {
                int next = (i + 1) % 3;
                float x1o = (float)Math.cos(angles[i]) * halfSize;
                float y1o = (float)Math.sin(angles[i]) * halfSize;
                float x2o = (float)Math.cos(angles[next]) * halfSize;
                float y2o = (float)Math.sin(angles[next]) * halfSize;
                float x1i = (float)Math.cos(angles[i]) * innerSize;
                float y1i = (float)Math.sin(angles[i]) * innerSize;
                float x2i = (float)Math.cos(angles[next]) * innerSize;
                float y2i = (float)Math.sin(angles[next]) * innerSize;
                vertexConsumer.vertex(matrix4f, x1o, y1o, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                vertexConsumer.vertex(matrix4f, x2o, y2o, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                vertexConsumer.vertex(matrix4f, x2i, y2i, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                vertexConsumer.vertex(matrix4f, x1o, y1o, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                vertexConsumer.vertex(matrix4f, x2i, y2i, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
                vertexConsumer.vertex(matrix4f, x1i, y1i, 0.0f).color(255, 255, 255, 255).uv(u, v).endVertex();
            }
        }
    }

    public static void renderStarRing(GuiGraphics guiGraphics, float centerX, float centerY, int width, int height) {
        ShaderInstance shader = ManboShaders.getStarRingShader();
        if (shader == null) {
            return;
        }
        shader.safeGetUniform("time").set(ManboShaders.getTimeValue());
        shader.safeGetUniform("screenSize").set(ManboShaders.getScreenSize().x, ManboShaders.getScreenSize().y);
        float ringRadiusX = (float)width * 1.05f;
        float ringRadiusY = (float)height * 0.9f;
        ManboFinalSwordVisualHandler.renderRingHalf(guiGraphics, centerX, centerY, ringRadiusX + 8.0f, ringRadiusY + 6.0f, 0.9f, -35.0f, 64, guiGraphics.bufferSource().getBuffer(ManboRenderTypes.star_ring), true);
        ManboFinalSwordVisualHandler.renderRingHalf(guiGraphics, centerX, centerY, ringRadiusX + 4.0f, ringRadiusY + 4.0f, 0.9f, -35.0f, 64, guiGraphics.bufferSource().getBuffer(ManboRenderTypes.star_ring), true);
    }

    public static void renderStarRingFront(GuiGraphics guiGraphics, float centerX, float centerY, int width, int height) {
        ShaderInstance shader = ManboShaders.getStarRingShader();
        if (shader == null) {
            return;
        }
        shader.safeGetUniform("time").set(ManboShaders.getTimeValue());
        shader.safeGetUniform("screenSize").set(ManboShaders.getScreenSize().x, ManboShaders.getScreenSize().y);
        float ringRadiusX = (float)width * 1.05f;
        float ringRadiusY = (float)height * 0.9f;
        ManboFinalSwordVisualHandler.renderRingHalf(guiGraphics, centerX, centerY, ringRadiusX + 8.0f, ringRadiusY + 6.0f, 0.9f, -35.0f, 64, guiGraphics.bufferSource().getBuffer(ManboRenderTypes.star_ring), false);
        ManboFinalSwordVisualHandler.renderRingHalf(guiGraphics, centerX, centerY, ringRadiusX + 4.0f, ringRadiusY + 4.0f, 0.9f, -35.0f, 64, guiGraphics.bufferSource().getBuffer(ManboRenderTypes.star_ring), false);
    }

    private static void renderRingHalf(GuiGraphics guiGraphics, float centerX, float centerY, float radiusX, float radiusY, float scale, float rotation, int segments, VertexConsumer vertexConsumer, boolean bottomHalf) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        ShaderInstance shader = ManboShaders.getStarRingShader();
        if (shader == null) {
            poseStack.popPose();
            return;
        }
        poseStack.translate(centerX, centerY, 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
        poseStack.translate(-centerX, -centerY, 0.0f);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        float tiltFactor = 1.0f - scale * 0.9f;
        for (int i = 0; i < segments; ++i) {
            boolean shouldRender;
            float angle1 = (float)((double)i * Math.PI * 2.0 / (double)segments);
            float angle2 = (float)((double)(i + 1) * Math.PI * 2.0 / (double)segments);
            float x1Outer = centerX + (float)Math.cos(angle1) * radiusX;
            float y1Outer = centerY + (float)Math.sin(angle1) * radiusY * tiltFactor;
            float x2Outer = centerX + (float)Math.cos(angle2) * radiusX;
            float y2Outer = centerY + (float)Math.sin(angle2) * radiusY * tiltFactor;
            float x1Inner = centerX + (float)Math.cos(angle1) * (radiusX - 5.0f);
            float y1Inner = centerY + (float)Math.sin(angle1) * (radiusY - 5.0f) * tiltFactor;
            float x2Inner = centerX + (float)Math.cos(angle2) * (radiusX - 5.0f);
            float y2Inner = centerY + (float)Math.sin(angle2) * (radiusY - 5.0f) * tiltFactor;
            float midY1 = (y1Outer + y1Inner) / 2.0f;
            float midY2 = (y2Outer + y2Inner) / 2.0f;
            float normalX1 = (midY1 - centerY) / radiusY;
            float normalY1 = -(x1Outer - centerX) / radiusX;
            float normalLen1 = (float)Math.sqrt(normalX1 * normalX1 + normalY1 * normalY1);
            if (normalLen1 > 0.001f) {
                normalX1 /= normalLen1;
                normalY1 /= normalLen1;
            }
            if (bottomHalf) {
                shouldRender = midY1 >= centerY;
            } else {
                boolean bl = shouldRender = midY1 < centerY;
            }
            if (!shouldRender) continue;
            vertexConsumer.vertex(matrix4f, x1Outer, y1Outer, 0.0f).color(255, 255, 255, 255).normal(matrix3f, normalX1, normalY1, 0.0f).endVertex();
            vertexConsumer.vertex(matrix4f, x2Outer, y2Outer, 0.0f).color(255, 255, 255, 255).normal(matrix3f, normalX1, normalY1, 0.0f).endVertex();
            vertexConsumer.vertex(matrix4f, x2Inner, y2Inner, 0.0f).color(255, 255, 255, 255).normal(matrix3f, normalX1, normalY1, 0.0f).endVertex();
            vertexConsumer.vertex(matrix4f, x1Inner, y1Inner, 0.0f).color(255, 255, 255, 255).normal(matrix3f, normalX1, normalY1, 0.0f).endVertex();
        }
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof AbstractContainerScreen)) {
            return;
        }
        AbstractContainerScreen containerScreen = (AbstractContainerScreen)screen;
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        double mouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight();
        for (Slot slot : containerScreen.getMenu().slots) {
            if (!slot.hasItem() || !slot.getItem().is((Item)ModItems.MANBO_FINAL_SWORD.get())) continue;
            float cx = containerScreen.getGuiLeft() + slot.x + 8;
            float cy = containerScreen.getGuiTop() + slot.y + 8;
            boolean hovered = mouseX >= (double)(containerScreen.getGuiLeft() + slot.x) && mouseX < (double)(containerScreen.getGuiLeft() + slot.x + 16) && mouseY >= (double)(containerScreen.getGuiTop() + slot.y) && mouseY < (double)(containerScreen.getGuiTop() + slot.y + 16);
            ManboFinalSwordVisualHandler.renderLightBrownGlow(guiGraphics, cx, cy, hovered);
        }
        if (wasRenderingFinalSwordTooltip) {
            wasRenderingFinalSwordTooltip = false;
            float centerX = (float)tooltipX + (float)tooltipWidth / 2.0f;
            float centerY = (float)tooltipY + (float)tooltipHeight / 2.0f;
            ManboFinalSwordVisualHandler.renderStarRingFront(guiGraphics, centerX, centerY, tooltipWidth, tooltipHeight);
            guiGraphics.bufferSource().endBatch(ManboRenderTypes.star_ring);
        }
        ManboFinalSwordVisualHandler.renderHearts(guiGraphics);
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
        ItemStack mainHand = mc.player.getMainHandItem();
        if (mainHand.is((Item)ModItems.MANBO_FINAL_SWORD.get())) {
            int slot = mc.player.getInventory().selected;
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            float cx = (float)sw / 2.0f - 91.0f + 1.0f + (float)(slot * 20) + 8.0f;
            float cy = sh - 22 + 1 + 8;
            ManboFinalSwordVisualHandler.renderLightBrownGlow(event.getGuiGraphics(), cx, cy, false);
        }
        if (mc.player.getOffhandItem().is((Item)ModItems.MANBO_FINAL_SWORD.get())) {
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            float cx = (float)sw / 2.0f + 91.0f + 1.0f + 8.0f;
            float cy = sh - 22 + 1 + 8;
            ManboFinalSwordVisualHandler.renderLightBrownGlow(event.getGuiGraphics(), cx, cy, false);
        }
    }

    private static void renderLightBrownGlow(GuiGraphics guiGraphics, float cx, float cy, boolean hovered) {
        int i;
        float time = (float)(System.nanoTime() - startTime) / 1.0E9f;
        float pulse = 1.0f + (float)Math.sin((double)(time * 1.5f) * Math.PI * 2.0) * 0.12f;
        float hoverScale = hovered ? 1.5f : 1.0f;
        float totalScale = 21.6f * pulse * hoverScale;
        int r = 222;
        int g = 184;
        int b = 135;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(cx, cy, 0.0f);
        poseStack.scale(totalScale, totalScale, 1.0f);
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vc = guiGraphics.bufferSource().getBuffer(ManboFinalSwordVisualHandler.getGlowAdditive());
        for (i = 0; i < 42; ++i) {
            int next = (i + 1) % 42;
            float angle1 = (float)(Math.PI * 2 * (double)i / 42.0);
            float angle2 = (float)(Math.PI * 2 * (double)next / 42.0);
            float offset1 = (float)Math.sin(time * 4.0f + (float)i * 0.5f) * 0.15f;
            float offset2 = (float)Math.sin(time * 4.0f + (float)next * 0.5f) * 0.15f;
            float innerR1 = 0.3f + offset1;
            float outerR1 = 0.6f + offset1;
            float innerR2 = 0.3f + offset2;
            float outerR2 = 0.6f + offset2;
            float ix1 = innerR1 * (float)Math.cos(angle1);
            float iy1 = innerR1 * (float)Math.sin(angle1);
            float ox1 = outerR1 * (float)Math.cos(angle1);
            float oy1 = outerR1 * (float)Math.sin(angle1);
            float ix2 = innerR2 * (float)Math.cos(angle2);
            float iy2 = innerR2 * (float)Math.sin(angle2);
            float ox2 = outerR2 * (float)Math.cos(angle2);
            float oy2 = outerR2 * (float)Math.sin(angle2);
            vc.vertex(matrix, ox1, oy1, 0.0f).color(r, g, b, 70).endVertex();
            vc.vertex(matrix, ox2, oy2, 0.0f).color(r, g, b, 70).endVertex();
            vc.vertex(matrix, ix2, iy2, 0.0f).color(r, g, b, 35).endVertex();
            vc.vertex(matrix, ox1, oy1, 0.0f).color(r, g, b, 70).endVertex();
            vc.vertex(matrix, ix2, iy2, 0.0f).color(r, g, b, 35).endVertex();
            vc.vertex(matrix, ix1, iy1, 0.0f).color(r, g, b, 35).endVertex();
        }
        for (i = 0; i < 9; ++i) {
            float angle = time * 1.0f + (float)((double)i * 2.0 * Math.PI / 9.0);
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotation(angle));
            float length = 1.15f;
            float minW = 0.01f;
            float maxW = 0.2625f;
            float x1 = -minW;
            float y1 = 0.0f;
            float x2 = minW;
            float y2 = 0.0f;
            float x3 = maxW;
            float y3 = length;
            float x4 = -maxW;
            float y4 = length;
            vc.vertex(poseStack.last().pose(), x1, y1, 0.0f).color(r, g, b, 160).endVertex();
            vc.vertex(poseStack.last().pose(), x2, y2, 0.0f).color(r, g, b, 160).endVertex();
            vc.vertex(poseStack.last().pose(), x3, y3, 0.0f).color(r, g, b, 0).endVertex();
            vc.vertex(poseStack.last().pose(), x1, y1, 0.0f).color(r, g, b, 160).endVertex();
            vc.vertex(poseStack.last().pose(), x3, y3, 0.0f).color(r, g, b, 0).endVertex();
            vc.vertex(poseStack.last().pose(), x4, y4, 0.0f).color(r, g, b, 0).endVertex();
            poseStack.popPose();
        }
        guiGraphics.bufferSource().endBatch(ManboFinalSwordVisualHandler.getGlowAdditive());
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is((Item)ModItems.MANBO_FINAL_SWORD.get())) {
            return;
        }
        wasRenderingFinalSwordTooltip = true;
        ManboFinalSwordVisualHandler.activateHeartEffect();
        tooltipX = event.getX();
        tooltipY = event.getY();
        tooltipWidth = event.getComponents().stream().mapToInt(c -> c.getWidth(event.getFont())).max().orElse(0) + 12;
        tooltipHeight = event.getComponents().stream().mapToInt(c -> c.getHeight()).sum() + 8;
        float centerX = (float)tooltipX + (float)tooltipWidth / 2.0f;
        float centerY = (float)tooltipY + (float)tooltipHeight / 2.0f;
        GuiGraphics guiGraphics = event.getGraphics();
        ManboFinalSwordVisualHandler.renderHexagramAt(tooltipX, tooltipY, tooltipWidth, tooltipHeight);
        guiGraphics.bufferSource().endBatch(ManboRenderTypes.hexagram_purpleorange);
        ManboFinalSwordVisualHandler.renderStarRing(guiGraphics, centerX, centerY, tooltipWidth, tooltipHeight);
        guiGraphics.bufferSource().endBatch(ManboRenderTypes.star_ring);
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        if (!player.getMainHandItem().is((Item)ModItems.MANBO_FINAL_SWORD.get()) && !player.getOffhandItem().is((Item)ModItems.MANBO_FINAL_SWORD.get())) {
            return;
        }
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        ManboFinalSwordVisualHandler.renderChibiBillboard(poseStack, bufferSource, player, event.getPartialTick());
    }

    private static void renderChibiBillboard(PoseStack poseStack, MultiBufferSource bufferSource, Player player, float partialTick) {
        double px = Mth.lerp((double)partialTick, (double)player.xOld, (double)player.getX());
        double py = Mth.lerp((double)partialTick, (double)player.yOld, (double)player.getY());
        double pz = Mth.lerp((double)partialTick, (double)player.zOld, (double)player.getZ());
        Vec3 look = player.getLookAngle();
        double backwardX = -look.x;
        double backwardZ = -look.z;
        double len = Math.sqrt(backwardX * backwardX + backwardZ * backwardZ);
        if (len < 0.001) {
            backwardX = 0.0;
            backwardZ = -1.0;
            len = 1.0;
        }
        double bx = px + (backwardX /= len) * 1.5;
        double by = py + 1.3;
        double bz = pz + (backwardZ /= len) * 1.5;
        poseStack.pushPose();
        poseStack.translate(bx - px, by - py, bz - pz);
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        float width = 1.8f;
        float height = 2.2f;
        poseStack.scale(width, height, 1.0f);
        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)CHIBI_TANNHAUSER_TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();
        vc.vertex(matrix, -0.5f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, 1.0f).endVertex();
        vc.vertex(matrix, 0.5f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, 1.0f).endVertex();
        vc.vertex(matrix, 0.5f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, 1.0f).endVertex();
        vc.vertex(matrix, -0.5f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, 1.0f).endVertex();
        vc.vertex(matrix, -0.5f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, -1.0f).endVertex();
        vc.vertex(matrix, -0.5f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, -1.0f).endVertex();
        vc.vertex(matrix, 0.5f, 1.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, -1.0f).endVertex();
        vc.vertex(matrix, 0.5f, 0.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0f, 0.0f, -1.0f).endVertex();
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (heartTooltipActive) {
            hearts.removeIf(h -> {
                h.y += h.speedY;
                h.x += h.speedX + Math.sin(h.swayOffset + (double)System.currentTimeMillis() * h.swaySpeed) * 1.2;
                --h.alpha;
                return h.alpha <= 0 || h.y > (double)(Minecraft.getInstance().getWindow().getGuiScaledHeight() + 30);
            });
            if (heartRandom.nextInt(2) == 0 && hearts.size() < 30) {
                Minecraft mc = Minecraft.getInstance();
                hearts.add(new OrangeHeartParticle(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight()));
            }
        }
        if (heartTooltipTimeout > 0 && --heartTooltipTimeout <= 0) {
            heartTooltipActive = false;
            hearts.clear();
        }
    }

    public static void activateHeartEffect() {
        heartTooltipActive = true;
        heartTooltipTimeout = 5;
        if (hearts.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            hearts.add(new OrangeHeartParticle(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight()));
        }
    }

    public static void renderHearts(GuiGraphics guiGraphics) {
        if (!heartTooltipActive || hearts.isEmpty()) {
            return;
        }
        heartTooltipTimeout = 5;
        for (OrangeHeartParticle h : hearts) {
            String heartChar = "\u2764";
            int a = Math.max(0, Math.min(255, h.alpha));
            int color = h.color & 0xFFFFFF | a << 24;
            guiGraphics.drawString(Minecraft.getInstance().font, heartChar, (int)h.x, (int)h.y, color, false);
        }
    }

    static {
        startTime = System.nanoTime();
        wasRenderingFinalSwordTooltip = false;
        CHIBI_TANNHAUSER_TEXTURE = new ResourceLocation("manbov2c", "textures/chibitannhauser2.png");
        heartTooltipActive = false;
        heartTooltipTimeout = 0;
        hearts = new ArrayList<OrangeHeartParticle>();
        heartRandom = new Random();
    }

    private static class OrangeHeartParticle {
        double x;
        double y;
        double speedY;
        double speedX;
        double swayOffset;
        double swaySpeed;
        float size;
        int alpha;
        int color;

        OrangeHeartParticle(double screenWidth, double screenHeight) {
            this.x = heartRandom.nextDouble() * screenWidth;
            this.y = -20.0 - heartRandom.nextDouble() * 40.0;
            this.speedY = 1.2 + heartRandom.nextDouble() * 1.2;
            this.speedX = (heartRandom.nextDouble() - 0.5) * 0.5;
            this.swayOffset = heartRandom.nextDouble() * Math.PI * 2.0;
            this.swaySpeed = 0.015 + heartRandom.nextDouble() * 0.02;
            this.size = 12.0f + heartRandom.nextFloat() * 10.0f;
            this.alpha = 180 + heartRandom.nextInt(75);
            int roll = heartRandom.nextInt(4);
            switch (roll) {
                case 0: {
                    this.color = -29696;
                    break;
                }
                case 1: {
                    this.color = -39424;
                    break;
                }
                case 2: {
                    this.color = -47872;
                    break;
                }
                default: {
                    this.color = -23296;
                }
            }
        }
    }
}

