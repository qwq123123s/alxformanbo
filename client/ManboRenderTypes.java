/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.renderer.RenderStateShard$CullStateShard
 *  net.minecraft.client.renderer.RenderStateShard$DepthTestStateShard
 *  net.minecraft.client.renderer.RenderStateShard$ShaderStateShard
 *  net.minecraft.client.renderer.RenderStateShard$TransparencyStateShard
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.RenderType$CompositeState
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.manbo.v2c.client;

import com.manbo.v2c.client.ManboShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class ManboRenderTypes {
    public static RenderType hexagram_rainbow;
    public static RenderType hexagram_pink;
    public static RenderType hexagram_purpleorange;
    public static RenderType star_ring;
    private static final RenderStateShard.ShaderStateShard HEXAGRAM_RAINBOW_SHADER;
    private static final RenderStateShard.ShaderStateShard HEXAGRAM_PINK_SHADER;
    private static final RenderStateShard.ShaderStateShard HEXAGRAM_PURPLEORANGE_SHADER;
    private static final RenderStateShard.ShaderStateShard STAR_RING_SHADER;
    private static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY;
    private static final RenderStateShard.CullStateShard NO_CULL;
    private static final RenderStateShard.DepthTestStateShard NO_DEPTH_TEST;

    public static void init() {
        hexagram_rainbow = RenderType.create((String)"manbo_hexagram_rainbow", (VertexFormat)DefaultVertexFormat.POSITION_COLOR_TEX, (VertexFormat.Mode)VertexFormat.Mode.TRIANGLES, (int)256, (boolean)false, (boolean)false, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(HEXAGRAM_RAINBOW_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false));
        hexagram_pink = RenderType.create((String)"manbo_hexagram_pink", (VertexFormat)DefaultVertexFormat.POSITION_COLOR_TEX, (VertexFormat.Mode)VertexFormat.Mode.TRIANGLES, (int)256, (boolean)false, (boolean)false, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(HEXAGRAM_PINK_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false));
        hexagram_purpleorange = RenderType.create((String)"manbo_hexagram_purpleorange", (VertexFormat)DefaultVertexFormat.POSITION_COLOR_TEX, (VertexFormat.Mode)VertexFormat.Mode.TRIANGLES, (int)256, (boolean)false, (boolean)false, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(HEXAGRAM_PURPLEORANGE_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false));
        star_ring = RenderType.create((String)"star_ring", (VertexFormat)DefaultVertexFormat.POSITION_COLOR_NORMAL, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)false, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(STAR_RING_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setDepthTestState(NO_DEPTH_TEST).createCompositeState(false));
    }

    static {
        HEXAGRAM_RAINBOW_SHADER = new RenderStateShard.ShaderStateShard(() -> ManboShaders.getManboHexagramRainbowShader());
        HEXAGRAM_PINK_SHADER = new RenderStateShard.ShaderStateShard(() -> ManboShaders.getManboHexagramPinkShader());
        HEXAGRAM_PURPLEORANGE_SHADER = new RenderStateShard.ShaderStateShard(() -> ManboShaders.getManboHexagramPurpleOrangeShader());
        STAR_RING_SHADER = new RenderStateShard.ShaderStateShard(() -> ManboShaders.getStarRingShader());
        TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)771);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        NO_CULL = new RenderStateShard.CullStateShard(false);
        NO_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("always", 519);
    }
}

