/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.GlStateManager$DestFactor
 *  com.mojang.blaze3d.platform.GlStateManager$SourceFactor
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.PoseStack
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
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.joml.Matrix4f
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ManboVoidSwordOverlayRenderer {
    private static final long CLOCK_BASE = System.nanoTime();
    private static final int RAY_COUNT = 9;
    private static final float RAY_LENGTH = 1.15f;
    private static final float RAY_MIN_WIDTH = 0.02f;
    private static final float RAY_MAX_WIDTH = 0.525f;
    private static final float COLOR_CHANGE_SPEED = 5000.0f;
    private static final float ROTATE_SPEED = 1.2f;
    private static final float PULSE_SPEED = 2.0f;
    private static final float PULSE_AMOUNT = 0.15f;
    private static final int RING_SEGMENTS = 42;
    private static final float RING_INNER_RADIUS = 0.3f;
    private static final float RING_OUTER_RADIUS = 0.6f;
    private static final RenderType GLOW_ADDITIVE = RenderType.create((String)"void_sword_glow", (VertexFormat)DefaultVertexFormat.POSITION_COLOR, (VertexFormat.Mode)VertexFormat.Mode.TRIANGLES, (int)0x200000, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader)).setTransparencyState(new RenderStateShard.TransparencyStateShard("additive", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    })).setCullState(new RenderStateShard.CullStateShard(false)).setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519)).setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false)).createCompositeState(false));

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof AbstractContainerScreen)) {
            return;
        }
        AbstractContainerScreen containerScreen = (AbstractContainerScreen)screen;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        double mouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight();
        for (Slot slot : containerScreen.getMenu().slots) {
            if (!slot.hasItem() || !slot.getItem().is((Item)ModItems.MANBO_VOID_SWORD.get())) continue;
            float cx = containerScreen.getGuiLeft() + slot.x + 8;
            float cy = containerScreen.getGuiTop() + slot.y + 8;
            boolean hovered = mouseX >= (double)(containerScreen.getGuiLeft() + slot.x) && mouseX < (double)(containerScreen.getGuiLeft() + slot.x + 16) && mouseY >= (double)(containerScreen.getGuiTop() + slot.y) && mouseY < (double)(containerScreen.getGuiTop() + slot.y + 16);
            ManboVoidSwordOverlayRenderer.renderVoidHalo(event.getGuiGraphics(), cx, cy, hovered);
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
        if (mainHand.is((Item)ModItems.MANBO_VOID_SWORD.get())) {
            int slot = mc.player.getInventory().selected;
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            cx = (float)sw / 2.0f - 91.0f + 1.0f + (float)(slot * 20) + 8.0f;
            cy = sh - 22 + 1 + 8;
            found = true;
        }
        if (!found && mc.player.getOffhandItem().is((Item)ModItems.MANBO_VOID_SWORD.get())) {
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            cx = (float)sw / 2.0f + 91.0f + 1.0f + 8.0f;
            cy = sh - 22 + 1 + 8;
            found = true;
        }
        if (!found) {
            return;
        }
        ManboVoidSwordOverlayRenderer.renderVoidHalo(event.getGuiGraphics(), cx, cy, false);
    }

    private static void renderVoidHalo(GuiGraphics guiGraphics, float cx, float cy, boolean hovered) {
        PoseStack poseStack = guiGraphics.pose();
        float time = (float)(System.nanoTime() - CLOCK_BASE) / 1.0E9f;
        float pulse = 1.0f + (float)Math.sin((double)(time * 2.0f) * Math.PI * 2.0) * 0.15f;
        float totalScale = 21.6f * pulse;
        poseStack.pushPose();
        poseStack.translate(cx, cy, 0.0f);
        poseStack.scale(totalScale, totalScale, 1.0f);
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vc = guiGraphics.bufferSource().getBuffer(GLOW_ADDITIVE);
        ManboVoidSwordOverlayRenderer.renderVoidRingWave(vc, matrix, time);
        for (int i = 0; i < 9; ++i) {
            float angle = time * 1.2f + (float)((double)i * 2.0 * Math.PI / 9.0);
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotation(angle));
            VoidColorInfo color = ManboVoidSwordOverlayRenderer.generateVoidColor(time, i, 9);
            ManboVoidSwordOverlayRenderer.renderGlowRay(vc, poseStack.last().pose(), 1.15f, 0.02f, 0.525f, color);
            poseStack.popPose();
        }
        guiGraphics.bufferSource().endBatch(GLOW_ADDITIVE);
        poseStack.popPose();
    }

    private static void renderGlowRay(VertexConsumer vc, Matrix4f matrix, float length, float minWidth, float maxWidth, VoidColorInfo color) {
        float x1 = -minWidth / 2.0f;
        float y1 = 0.0f;
        float x2 = minWidth / 2.0f;
        float y2 = 0.0f;
        float x3 = maxWidth / 2.0f;
        float y3 = length;
        float x4 = -maxWidth / 2.0f;
        float y4 = length;
        vc.vertex(matrix, x1, y1, 0.0f).color(color.r1, color.g1, color.b1, 160).endVertex();
        vc.vertex(matrix, x2, y2, 0.0f).color(color.r1, color.g1, color.b1, 160).endVertex();
        vc.vertex(matrix, x3, y3, 0.0f).color(color.r2, color.g2, color.b2, 0).endVertex();
        vc.vertex(matrix, x1, y1, 0.0f).color(color.r1, color.g1, color.b1, 160).endVertex();
        vc.vertex(matrix, x3, y3, 0.0f).color(color.r2, color.g2, color.b2, 0).endVertex();
        vc.vertex(matrix, x4, y4, 0.0f).color(color.r2, color.g2, color.b2, 0).endVertex();
    }

    private static void renderVoidRingWave(VertexConsumer vc, Matrix4f matrix, float time) {
        int alpha = 80;
        for (int i = 0; i < 42; ++i) {
            int nextI = (i + 1) % 42;
            float angle1 = (float)(Math.PI * 2 * (double)i / 42.0);
            float angle2 = (float)(Math.PI * 2 * (double)nextI / 42.0);
            float offset1 = ManboVoidSwordOverlayRenderer.calculateWaveOffset(time * 4.0f, i);
            float offset2 = ManboVoidSwordOverlayRenderer.calculateWaveOffset(time * 4.0f, nextI);
            float innerR1 = 0.3f + offset1;
            float outerR1 = 0.6f + offset1;
            float innerR2 = 0.3f + offset2;
            float outerR2 = 0.6f + offset2;
            float innerX1 = (float)((double)innerR1 * Math.cos(angle1));
            float innerY1 = (float)((double)innerR1 * Math.sin(angle1));
            float outerX1 = (float)((double)outerR1 * Math.cos(angle1));
            float outerY1 = (float)((double)outerR1 * Math.sin(angle1));
            float innerX2 = (float)((double)innerR2 * Math.cos(angle2));
            float innerY2 = (float)((double)innerR2 * Math.sin(angle2));
            float outerX2 = (float)((double)outerR2 * Math.cos(angle2));
            float outerY2 = (float)((double)outerR2 * Math.sin(angle2));
            boolean isPurpleTone = i % 2 == 0;
            int r = isPurpleTone ? 180 : 220;
            int g = isPurpleTone ? 0 : 40;
            int b = isPurpleTone ? 255 : 200;
            vc.vertex(matrix, innerX1, innerY1, 0.0f).color(r, g, b, alpha).endVertex();
            vc.vertex(matrix, outerX1, outerY1, 0.0f).color(r, g, b, alpha).endVertex();
            vc.vertex(matrix, innerX2, innerY2, 0.0f).color(r, g, b, alpha).endVertex();
            vc.vertex(matrix, outerX1, outerY1, 0.0f).color(r, g, b, alpha).endVertex();
            vc.vertex(matrix, outerX2, outerY2, 0.0f).color(r, g, b, alpha).endVertex();
            vc.vertex(matrix, innerX2, innerY2, 0.0f).color(r, g, b, alpha).endVertex();
        }
    }

    private static float calculateWaveOffset(float time, int index) {
        return (float)(Math.sin(time * 3.5f + (float)index * 1.0f) * (double)0.17f + Math.sin(time * 3.0f + (float)index * 0.5f) * (double)0.13f + Math.cos(time * 3.5f * 0.7f + (float)index * 1.0f * 0.5f) * (double)0.17f * 0.5);
    }

    private static VoidColorInfo generateVoidColor(float time, int index, int count) {
        float colorPhaseOffset = (float)((double)index * 360.0 / (double)count);
        float hue1 = (time * 5000.0f + colorPhaseOffset) % 360.0f;
        float hue2 = (time * 5000.0f + colorPhaseOffset + 60.0f) % 360.0f;
        float baseHue = 0.78f;
        float hueRange = 0.1f;
        float h1 = (hue1 / 360.0f * hueRange + baseHue) % 1.0f;
        float h2 = (hue2 / 360.0f * hueRange + baseHue) % 1.0f;
        int raw1 = Color.HSBtoRGB(h1, 1.0f, 1.0f);
        int raw2 = Color.HSBtoRGB(h2, 1.0f, 1.0f);
        int r1 = raw1 >> 16 & 0xFF;
        int g1 = raw1 >> 8 & 0xFF;
        int b1 = raw1 & 0xFF;
        int r2 = raw2 >> 16 & 0xFF;
        int g2 = raw2 >> 8 & 0xFF;
        int b2 = raw2 & 0xFF;
        return new VoidColorInfo(r1, g1, b1, r2, g2, b2);
    }

    private static class VoidColorInfo {
        int r1;
        int g1;
        int b1;
        int r2;
        int g2;
        int b2;

        VoidColorInfo(int r1, int g1, int b1, int r2, int g2, int b2) {
            this.r1 = r1;
            this.g1 = g1;
            this.b1 = b1;
            this.r2 = r2;
            this.g2 = g2;
            this.b2 = b2;
        }
    }
}

