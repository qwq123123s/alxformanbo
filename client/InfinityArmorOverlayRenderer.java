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
public class InfinityArmorOverlayRenderer {
    private static final long CLOCK_BASE = System.nanoTime();
    private static final int RAY_COUNT = 7;
    private static final float RAY_LENGTH = 1.0f;
    private static final float RAY_MIN_WIDTH = 0.02f;
    private static final float RAY_MAX_WIDTH = 0.525f;
    private static final float ROTATE_SPEED = 0.5f;
    private static final float PULSE_SPEED = 1.0f;
    private static final float PULSE_AMOUNT = 0.12f;
    private static final float TOOLTIP_SCALE_BOOST = 1.4f;
    private static final int RING_SEGMENTS = 42;
    private static final float RING_INNER_RADIUS = 0.3f;
    private static final float RING_OUTER_RADIUS = 0.6f;
    private static final RenderType GLOW_ADDITIVE = RenderType.create((String)"infinity_armor_glow", (VertexFormat)DefaultVertexFormat.POSITION_COLOR, (VertexFormat.Mode)VertexFormat.Mode.TRIANGLES, (int)0x200000, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorShader)).setTransparencyState(new RenderStateShard.TransparencyStateShard("additive", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    })).setCullState(new RenderStateShard.CullStateShard(false)).setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519)).setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false)).createCompositeState(false));

    private static boolean isUpgradeArmor(ItemStack stack) {
        return stack.is((Item)ModItems.MANBO_INFINITY_UPGRADE_HELMET.get()) || stack.is((Item)ModItems.MANBO_INFINITY_UPGRADE_CHESTPLATE.get()) || stack.is((Item)ModItems.MANBO_INFINITY_UPGRADE_LEGGINGS.get()) || stack.is((Item)ModItems.MANBO_INFINITY_UPGRADE_BOOTS.get());
    }

    private static boolean isGoldenArmor(ItemStack stack) {
        return stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_HELMET.get()) || stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_CHESTPLATE.get()) || stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_LEGGINGS.get()) || stack.is((Item)ModItems.MANBO_INFINITY_GOLDEN_BOOTS.get());
    }

    private static boolean isAlepbArmor(ItemStack stack) {
        return stack.is((Item)ModItems.MANBO_INFINITY_ALEPB_HELMET.get()) || stack.is((Item)ModItems.MANBO_INFINITY_ALEPB_CHESTPLATE.get()) || stack.is((Item)ModItems.MANBO_INFINITY_ALEPB_LEGGINGS.get()) || stack.is((Item)ModItems.MANBO_INFINITY_ALEPB_BOOTS.get());
    }

    private static boolean isUnknownArmor(ItemStack stack) {
        return stack.is((Item)ModItems.MANBO_INFINITY_UNKNOWN_HELMET.get()) || stack.is((Item)ModItems.MANBO_INFINITY_UNKNOWN_CHESTPLATE.get()) || stack.is((Item)ModItems.MANBO_INFINITY_UNKNOWN_LEGGINGS.get()) || stack.is((Item)ModItems.MANBO_INFINITY_UNKNOWN_BOOTS.get());
    }

    private static boolean isInfinityArmor(ItemStack stack) {
        return InfinityArmorOverlayRenderer.isUpgradeArmor(stack) || InfinityArmorOverlayRenderer.isGoldenArmor(stack) || InfinityArmorOverlayRenderer.isAlepbArmor(stack) || InfinityArmorOverlayRenderer.isUnknownArmor(stack);
    }

    private static int getArmorSetIndex(ItemStack stack) {
        if (InfinityArmorOverlayRenderer.isUpgradeArmor(stack)) {
            return 0;
        }
        if (InfinityArmorOverlayRenderer.isGoldenArmor(stack)) {
            return 1;
        }
        if (InfinityArmorOverlayRenderer.isAlepbArmor(stack)) {
            return 2;
        }
        if (InfinityArmorOverlayRenderer.isUnknownArmor(stack)) {
            return 3;
        }
        return -1;
    }

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
            if (!slot.hasItem() || !InfinityArmorOverlayRenderer.isInfinityArmor(slot.getItem())) continue;
            float cx = containerScreen.getGuiLeft() + slot.x + 8;
            float cy = containerScreen.getGuiTop() + slot.y + 8;
            boolean hovered = mouseX >= (double)(containerScreen.getGuiLeft() + slot.x) && mouseX < (double)(containerScreen.getGuiLeft() + slot.x + 16) && mouseY >= (double)(containerScreen.getGuiTop() + slot.y) && mouseY < (double)(containerScreen.getGuiTop() + slot.y + 16);
            InfinityArmorOverlayRenderer.renderHalo(event.getGuiGraphics(), cx, cy, hovered, InfinityArmorOverlayRenderer.getArmorSetIndex(slot.getItem()));
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
        for (int slot = 0; slot < 9; ++slot) {
            ItemStack stack = mc.player.getInventory().getItem(slot);
            if (!InfinityArmorOverlayRenderer.isInfinityArmor(stack)) continue;
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            float cx = (float)sw / 2.0f - 91.0f + 1.0f + (float)(slot * 20) + 8.0f;
            float cy = sh - 22 + 1 + 8;
            InfinityArmorOverlayRenderer.renderHalo(event.getGuiGraphics(), cx, cy, false, InfinityArmorOverlayRenderer.getArmorSetIndex(stack));
        }
        ItemStack offhand = mc.player.getOffhandItem();
        if (InfinityArmorOverlayRenderer.isInfinityArmor(offhand)) {
            int sw = mc.getWindow().getGuiScaledWidth();
            int sh = mc.getWindow().getGuiScaledHeight();
            float cx = (float)sw / 2.0f + 91.0f + 1.0f + 8.0f;
            float cy = sh - 22 + 1 + 8;
            InfinityArmorOverlayRenderer.renderHalo(event.getGuiGraphics(), cx, cy, false, InfinityArmorOverlayRenderer.getArmorSetIndex(offhand));
        }
    }

    private static void renderHalo(GuiGraphics guiGraphics, float cx, float cy, boolean hovered, int setIndex) {
        PoseStack poseStack = guiGraphics.pose();
        float time = (float)(System.nanoTime() - CLOCK_BASE) / 1.0E9f;
        float pulse = 1.0f + (float)Math.sin((double)(time * 1.0f) * Math.PI * 2.0) * 0.12f;
        float hoverScale = hovered ? 1.4f : 1.0f;
        float totalScale = 21.6f * pulse * hoverScale;
        poseStack.pushPose();
        poseStack.translate(cx, cy, 0.0f);
        poseStack.scale(totalScale, totalScale, 1.0f);
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer vc = guiGraphics.bufferSource().getBuffer(GLOW_ADDITIVE);
        InfinityArmorOverlayRenderer.renderRingWave(vc, matrix, time, setIndex);
        for (int i = 0; i < 7; ++i) {
            float angle = time * 0.5f + (float)((double)i * 2.0 * Math.PI / 7.0);
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotation(angle));
            boolean isEven = i % 2 == 0;
            ColorInfo color = InfinityArmorOverlayRenderer.generateTimeBasedColor(time, i, 7, setIndex, isEven);
            InfinityArmorOverlayRenderer.renderGlowRay(vc, poseStack.last().pose(), 1.0f, 0.02f, 0.525f, color);
            poseStack.popPose();
        }
        guiGraphics.bufferSource().endBatch(GLOW_ADDITIVE);
        poseStack.popPose();
    }

    private static void renderGlowRay(VertexConsumer vc, Matrix4f matrix, float length, float minWidth, float maxWidth, ColorInfo color) {
        float x1 = -minWidth / 2.0f;
        float y1 = 0.0f;
        float x2 = minWidth / 2.0f;
        float y2 = 0.0f;
        float x3 = maxWidth / 2.0f;
        float y3 = length;
        float x4 = -maxWidth / 2.0f;
        float y4 = length;
        vc.vertex(matrix, x1, y1, 0.0f).color(color.r1, color.g1, color.b1, 60).endVertex();
        vc.vertex(matrix, x2, y2, 0.0f).color(color.r1, color.g1, color.b1, 60).endVertex();
        vc.vertex(matrix, x3, y3, 0.0f).color(color.r2, color.g2, color.b2, 0).endVertex();
        vc.vertex(matrix, x1, y1, 0.0f).color(color.r1, color.g1, color.b1, 60).endVertex();
        vc.vertex(matrix, x3, y3, 0.0f).color(color.r2, color.g2, color.b2, 0).endVertex();
        vc.vertex(matrix, x4, y4, 0.0f).color(color.r2, color.g2, color.b2, 0).endVertex();
    }

    private static void renderRingWave(VertexConsumer vc, Matrix4f matrix, float time, int setIndex) {
        int alpha = 30;
        for (int i = 0; i < 42; ++i) {
            int g;
            int r;
            int nextI = (i + 1) % 42;
            float angle1 = (float)(Math.PI * 2 * (double)i / 42.0);
            float angle2 = (float)(Math.PI * 2 * (double)nextI / 42.0);
            float offset1 = InfinityArmorOverlayRenderer.calculateWaveOffset(time * 4.0f, i);
            float offset2 = InfinityArmorOverlayRenderer.calculateWaveOffset(time * 4.0f, nextI);
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
            boolean isEven = i % 2 == 0;
            int b = switch (setIndex) {
                case 0 -> {
                    if (isEven) {
                        r = 30;
                        g = 30;
                        yield 30;
                    }
                    r = 200;
                    g = 200;
                    yield 200;
                }
                case 1 -> {
                    if (isEven) {
                        r = 255;
                        g = 215;
                        yield 0;
                    }
                    r = 255;
                    g = 255;
                    yield 200;
                }
                case 2 -> {
                    if (isEven) {
                        r = 0;
                        g = 100;
                        yield 180;
                    }
                    r = 100;
                    g = 200;
                    yield 255;
                }
                case 3 -> {
                    if (isEven) {
                        r = 180;
                        g = 0;
                        yield 255;
                    }
                    r = 255;
                    g = 100;
                    yield 200;
                }
                default -> {
                    r = 255;
                    g = 255;
                    yield 255;
                }
            };
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

    private static ColorInfo generateTimeBasedColor(float time, int index, int count, int setIndex, boolean isEven) {
        float colorPhaseOffset = (float)((double)index * 360.0 / (double)count);
        float hue1 = colorPhaseOffset % 360.0f;
        float hue2 = (colorPhaseOffset + 60.0f) % 360.0f;
        switch (setIndex) {
            case 0: {
                float baseHue = 0.0f;
                float hueRange = 0.02f;
                float h1 = (hue1 / 360.0f * hueRange + baseHue) % 1.0f;
                float h2 = (hue2 / 360.0f * hueRange + baseHue) % 1.0f;
                int color1 = Color.HSBtoRGB(h1, 0.0f, isEven ? 0.3f : 0.8f);
                int color2 = Color.HSBtoRGB(h2, 0.0f, isEven ? 0.5f : 0.9f);
                return InfinityArmorOverlayRenderer.extractColorInfo(color1, color2);
            }
            case 1: {
                float baseHue = 0.1f;
                float hueRange = 0.08f;
                float h1 = (hue1 / 360.0f * hueRange + baseHue) % 1.0f;
                float h2 = (hue2 / 360.0f * hueRange + baseHue) % 1.0f;
                int color1 = Color.HSBtoRGB(h1, 0.9f, isEven ? 0.8f : 1.0f);
                int color2 = Color.HSBtoRGB(h2, 0.8f, isEven ? 0.9f : 1.0f);
                return InfinityArmorOverlayRenderer.extractColorInfo(color1, color2);
            }
            case 2: {
                float baseHue = 0.55f;
                float hueRange = 0.1f;
                float h1 = (hue1 / 360.0f * hueRange + baseHue) % 1.0f;
                float h2 = (hue2 / 360.0f * hueRange + baseHue) % 1.0f;
                int color1 = Color.HSBtoRGB(h1, 0.8f, isEven ? 0.6f : 0.9f);
                int color2 = Color.HSBtoRGB(h2, 0.7f, isEven ? 0.7f : 1.0f);
                return InfinityArmorOverlayRenderer.extractColorInfo(color1, color2);
            }
        }
        float baseHue = 0.75f;
        float hueRange = 0.3f;
        float h1 = (hue1 / 360.0f * hueRange + baseHue) % 1.0f;
        float h2 = (hue2 / 360.0f * hueRange + baseHue) % 1.0f;
        int color1 = Color.HSBtoRGB(h1, 0.9f, isEven ? 0.7f : 1.0f);
        int color2 = Color.HSBtoRGB(h2, 0.8f, isEven ? 0.8f : 1.0f);
        return InfinityArmorOverlayRenderer.extractColorInfo(color1, color2);
    }

    private static ColorInfo extractColorInfo(int color1, int color2) {
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        return new ColorInfo(r1, g1, b1, r2, g2, b2);
    }

    private static class ColorInfo {
        int r1;
        int g1;
        int b1;
        int r2;
        int g2;
        int b2;

        ColorInfo(int r1, int g1, int b1, int r2, int g2, int b2) {
            this.r1 = r1;
            this.g1 = g1;
            this.b1 = b1;
            this.r2 = r2;
            this.g2 = g2;
            this.b2 = b2;
        }
    }
}

