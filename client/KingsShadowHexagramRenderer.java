/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderTooltipEvent$Pre
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.joml.Matrix4f
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.client.ManboRenderTypes;
import com.manbo.v2c.client.ManboShaders;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class KingsShadowHexagramRenderer {
    private static boolean isKingsShadowTooltip = false;
    private static int tooltipX = 0;
    private static int tooltipY = 0;
    private static int tooltipWidth = 0;
    private static int tooltipHeight = 0;
    private static final int RENDER_LAYER_BACK = 180;
    private static final int RENDER_LAYER_FRONT = 220;

    @SubscribeEvent
    public static void onTooltipEvent(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is((Item)ModItems.KINGS_SHADOW.get())) {
            isKingsShadowTooltip = false;
            return;
        }
        if (event.isCanceled()) {
            return;
        }
        isKingsShadowTooltip = true;
        tooltipX = event.getX();
        tooltipY = event.getY();
        tooltipWidth = event.getComponents().stream().mapToInt(c -> c.getWidth(event.getFont())).max().orElse(0) + 12;
        tooltipHeight = event.getComponents().stream().mapToInt(c -> c.getHeight()).sum() + 8;
        KingsShadowHexagramRenderer.renderHexagramNow();
    }

    public static void renderHexagramAt(int x, int y, int w, int h) {
        isKingsShadowTooltip = true;
        tooltipX = x;
        tooltipY = y;
        tooltipWidth = w;
        tooltipHeight = h;
        KingsShadowHexagramRenderer.renderHexagramNow();
    }

    public static void renderHexagramNow() {
        ShaderInstance shader = ManboShaders.getManboHexagramPinkShader();
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
        VertexConsumer vertexConsumer = bufferSource.getBuffer(ManboRenderTypes.hexagram_pink);
        KingsShadowHexagramRenderer.drawThickHexagram(centerX, centerY, halfSize, innerSize, rotation, 180, vertexConsumer, upAngles, downAngles);
        bufferSource.endBatch(ManboRenderTypes.hexagram_pink);
        float innerScale = 0.92f;
        float innerHalfSize = halfSize * innerScale;
        float innerLineWidth = Math.max(innerHalfSize * 0.22f, 6.0f);
        float innerInnerSize = Math.max(innerHalfSize - innerLineWidth, innerHalfSize * 0.3f);
        shader.safeGetUniform("radius").set(innerHalfSize);
        shader.safeGetUniform("rotation").set(rotation + 0.08f);
        VertexConsumer vertexConsumer2 = bufferSource.getBuffer(ManboRenderTypes.hexagram_pink);
        KingsShadowHexagramRenderer.drawThickHexagram(centerX, centerY, innerHalfSize, innerInnerSize, rotation + 0.08f, 220, vertexConsumer2, upAngles, downAngles);
        bufferSource.endBatch(ManboRenderTypes.hexagram_pink);
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
}

