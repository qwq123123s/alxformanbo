/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.client.event.RegisterShadersEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.joml.Vector2f
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.client;

import com.manbo.v2c.client.ManboRenderTypes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OnlyIn(value=Dist.CLIENT)
@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ManboShaders {
    private static final Logger LOGGER = LoggerFactory.getLogger((String)"ManboShaders");
    private static ShaderInstance manboHexagramRainbow;
    private static ShaderInstance manboHexagramPink;
    private static ShaderInstance manboHexagramPurpleOrange;
    private static ShaderInstance starRing;
    private static long startTime;
    private static Vector2f screenSize;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        LOGGER.info("RegisterShadersEvent fired, attempting to load shaders...");
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), "manbov2c:manbo_hexagram_rainbow", DefaultVertexFormat.POSITION_COLOR_TEX), shader -> {
                manboHexagramRainbow = shader;
                LOGGER.info("manbo_hexagram_rainbow shader loaded SUCCESSFULLY! instance={}", shader);
            });
        }
        catch (IOException e) {
            LOGGER.error("Failed to load manbo_hexagram_rainbow shader!", (Throwable)e);
        }
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), "manbov2c:manbo_hexagram_pink", DefaultVertexFormat.POSITION_COLOR_TEX), shader -> {
                manboHexagramPink = shader;
                LOGGER.info("manbo_hexagram_pink shader loaded SUCCESSFULLY! instance={}", shader);
            });
        }
        catch (IOException e) {
            LOGGER.error("Failed to load manbo_hexagram_pink shader!", (Throwable)e);
        }
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), "manbov2c:manbo_hexagram_purpleorange", DefaultVertexFormat.POSITION_COLOR_TEX), shader -> {
                manboHexagramPurpleOrange = shader;
                LOGGER.info("manbo_hexagram_purpleorange shader loaded SUCCESSFULLY! instance={}", shader);
            });
        }
        catch (IOException e) {
            LOGGER.error("Failed to load manbo_hexagram_purpleorange shader!", (Throwable)e);
        }
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), "manbov2c:star_ring", DefaultVertexFormat.POSITION_COLOR_NORMAL), shader -> {
                starRing = shader;
                LOGGER.info("star_ring shader loaded SUCCESSFULLY! instance={}", shader);
            });
        }
        catch (IOException e) {
            LOGGER.error("Failed to load star_ring shader!", (Throwable)e);
        }
        ManboRenderTypes.init();
    }

    public static ShaderInstance getManboHexagramRainbowShader() {
        return manboHexagramRainbow;
    }

    public static ShaderInstance getManboHexagramPinkShader() {
        return manboHexagramPink;
    }

    public static ShaderInstance getManboHexagramPurpleOrangeShader() {
        return manboHexagramPurpleOrange;
    }

    public static ShaderInstance getStarRingShader() {
        return starRing;
    }

    public static float getTimeValue() {
        return (float)(System.currentTimeMillis() - startTime) / 1000.0f;
    }

    public static Vector2f getScreenSize() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getWindow() != null) {
            screenSize.set((float)mc.getWindow().getWidth(), (float)mc.getWindow().getHeight());
        }
        return screenSize;
    }

    static {
        startTime = System.currentTimeMillis();
        screenSize = new Vector2f(1920.0f, 1080.0f);
    }
}

