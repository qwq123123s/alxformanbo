/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ManbohajimiSuperSwordOverlay {
    private static final ResourceLocation SHIBAO_BG = new ResourceLocation("manbov2c", "textures/item/shibao_background.png");
    private static final ResourceLocation HAJIMI_BG = new ResourceLocation("manbov2c", "textures/item/hajimi_bg.png");
    private static final float SHIBAO_NATIVE_W = 67.0f;
    private static final float SHIBAO_NATIVE_H = 82.0f;
    private static final float SHIBAO_DISPLAY_W = 130.0f;
    private static final float SHIBAO_DISPLAY_H = 159.10448f;
    private static final float HAJIMI_NATIVE_W = 308.0f;
    private static final float HAJIMI_NATIVE_H = 512.0f;
    private static final float HAJIMI_DISPLAY_W = 150.0f;
    private static final float HAJIMI_DISPLAY_H = 249.35065f;

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        int bgY;
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
        boolean drawShibao = false;
        boolean drawHajimi = false;
        for (Slot slot : containerScreen.getMenu().slots) {
            boolean hovered;
            if (!slot.hasItem()) continue;
            ItemStack stack = slot.getItem();
            float sx = containerScreen.getGuiLeft() + slot.x;
            float sy = containerScreen.getGuiTop() + slot.y;
            boolean bl = hovered = mouseX >= (double)sx && mouseX < (double)(sx + 16.0f) && mouseY >= (double)sy && mouseY < (double)(sy + 16.0f);
            if (hovered && stack.is((Item)ModItems.MANBOHAJIMI_SUPER_SWORD.get())) {
                drawShibao = true;
                break;
            }
            if (!hovered || !stack.is((Item)ModItems.HAJIMI_FEET.get())) continue;
            drawHajimi = true;
            break;
        }
        if (!drawShibao && !drawHajimi) {
            return;
        }
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        if (drawShibao) {
            int iw = 130;
            int ih = 159;
            int bgX = (int)mouseX + 12;
            bgY = (int)mouseY - 12;
            if (bgX + iw > sw) {
                bgX = (int)mouseX - iw - 8;
            }
            if (bgY + ih > sh) {
                bgY = (int)mouseY - ih - 8;
            }
            if (bgY < 0) {
                bgY = (int)mouseY + 12;
            }
            ManbohajimiSuperSwordOverlay.renderBackground(event.getGuiGraphics(), bgX, bgY, SHIBAO_BG, 130.0f, 159.10448f, 67.0f, 82.0f, 0.65f);
        }
        if (drawHajimi) {
            int iw = 150;
            int ih = 249;
            int bgX = (int)mouseX + 12;
            bgY = (int)mouseY - 12;
            if (bgX + iw > sw) {
                bgX = (int)mouseX - iw - 8;
            }
            if (bgY + ih > sh) {
                bgY = (int)mouseY - ih - 8;
            }
            if (bgY < 0) {
                bgY = (int)mouseY + 12;
            }
            ManbohajimiSuperSwordOverlay.renderBackground(event.getGuiGraphics(), bgX, bgY, HAJIMI_BG, 150.0f, 249.35065f, 308.0f, 512.0f, 0.5f);
        }
    }

    private static void renderBackground(GuiGraphics graphics, int x, int y, ResourceLocation texture, float displayW, float displayH, float nativeW, float nativeH, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate((float)x, (float)y, 0.0f);
        float scaleX = displayW / nativeW;
        float scaleY = displayH / nativeH;
        pose.scale(scaleX, scaleY, 1.0f);
        graphics.blit(texture, 0, 0, 0.0f, 0.0f, (int)nativeW, (int)nativeH, (int)nativeW, (int)nativeH);
        pose.popPose();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
    }
}

