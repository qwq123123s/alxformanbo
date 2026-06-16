/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.InputEvent$Key
 *  net.minecraftforge.client.event.InputEvent$MouseButton
 *  net.minecraftforge.client.event.RenderTooltipEvent$Color
 *  net.minecraftforge.client.event.RenderTooltipEvent$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.event.entity.player.ItemTooltipEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModBlocks;
import com.manbo.v2c.ModItems;
import com.manbo.v2c.client.KingsShadowHexagramRenderer;
import com.manbo.v2c.item.weapons.ManboVoidSword;
import com.manbo.v2c.item.weapons.ManbohajimiSuperSword;
import com.manbo.v2c.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "manbov2c", value = { Dist.CLIENT }, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTooltipHandler {
    private static int tooltipPage = 0;
    private static ItemStack currentKingsShadowStack = ItemStack.EMPTY;
    private static int totalPages = 1;
    private static int tooltipX = 0;
    private static int tooltipY = 0;
    private static int tooltipWidth = 0;
    private static int tooltipHeight = 0;
    private static String cachedTitleText = null;
    private static List<String> cachedFixedLines = new ArrayList<String>();
    private static List<String> cachedNarrativeLines = new ArrayList<String>();
    private static List<Integer> cachedNarrativeTypes = new ArrayList<Integer>();
    private static List<String> cachedStatLines = new ArrayList<String>();
    private static List<String> cachedEnchantTexts = new ArrayList<String>();
    private static List<Component> cachedTooltipContent = null;
    private static int customTooltipX = 0;
    private static int customTooltipY = 0;
    private static int customTooltipWidth = 0;
    private static int customTooltipHeight = 0;
    private static boolean isHoveringKingsShadow = false;
    private static long lastTooltipActiveTime = 0L;
    private static final long TOOLTIP_TIMEOUT_MS = 3000L;
    private static long lastClickTime = 0L;
    private static final long CLICK_DEBOUNCE_MS = 300L;
    private static boolean persistenceActive = false;
    private static int finalSwordPage = 0;
    private static int finalSwordTotalPages = 1;
    private static List<String> finalSwordNarrativeLines = new ArrayList<String>();
    private static List<String> finalSwordStatLines = new ArrayList<String>();
    private static boolean isHoveringFinalSword = false;
    private static long finalSwordLastActiveTime = 0L;
    private static final long FINAL_SWORD_TOOLTIP_TIMEOUT_MS = 500L;

    private static boolean isCurrentStackKingsShadow(ItemStack stack) {
        if (currentKingsShadowStack.isEmpty()) {
            return false;
        }
        return currentKingsShadowStack.getItem() == stack.getItem();
    }

    private static int getDynamicRainbowColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.2;
        float hue = (float) ((timeOffset + (double) offset) % 1.0);
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }

    private static int getDynamicYellowGreenColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(255, 255, 0);
        Color color2 = new Color(0, 255, 150);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicRedGreenColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(255, 0, 0);
        Color color2 = new Color(0, 255, 0);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicRedPurpleColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(255, 0, 0);
        Color color2 = new Color(180, 0, 255);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicRedOrangeColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(255, 50, 0);
        Color color2 = new Color(255, 180, 0);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicBlackOrangeColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(0, 0, 0);
        Color color2 = new Color(255, 140, 0);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicBlackSilverColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(30, 30, 30);
        Color color2 = new Color(200, 200, 200);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicGoldShinyColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.8;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        float shiny = factor * factor;
        Color color1 = new Color(180, 130, 0);
        Color color2 = new Color(255, 230, 100);
        int r = (int) ((float) color1.getRed() + shiny * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + shiny * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + shiny * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicSeaBlueSkyBlueColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(0, 100, 180);
        Color color2 = new Color(100, 200, 255);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicBlackWhiteColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 3.0;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(0, 0, 0);
        Color color2 = new Color(255, 255, 255);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicBlueWhiteColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(0, 100, 255);
        Color color2 = new Color(200, 230, 255);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicOrangeCyanColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color orange = new Color(255, 165, 0);
        Color cyan = new Color(0, 255, 255);
        int r = (int) ((float) orange.getRed() + factor * (float) (cyan.getRed() - orange.getRed()));
        int g = (int) ((float) orange.getGreen() + factor * (float) (cyan.getGreen() - orange.getGreen()));
        int b = (int) ((float) orange.getBlue() + factor * (float) (cyan.getBlue() - orange.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicGreenPurpleColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color green = new Color(0, 255, 100);
        Color purple = new Color(180, 0, 255);
        int r = (int) ((float) green.getRed() + factor * (float) (purple.getRed() - green.getRed()));
        int g = (int) ((float) green.getGreen() + factor * (float) (purple.getGreen() - green.getGreen()));
        int b = (int) ((float) green.getBlue() + factor * (float) (purple.getBlue() - green.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static Component createGreenBlueGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.2;
        Color green = new Color(0, 255, 100);
        Color blue = new Color(0, 150, 255);
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float factor = (float) ((Math.cos(timeOffset + (double) i * 0.2) + 1.0) / 2.0);
            int r = (int) ((float) green.getRed() + factor * (float) (blue.getRed() - green.getRed()));
            int g = (int) ((float) green.getGreen() + factor * (float) (blue.getGreen() - green.getGreen()));
            int b = (int) ((float) green.getBlue() + factor * (float) (blue.getBlue() - green.getBlue()));
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(new Color(r, g, b).getRGB())));
        }
        return result;
    }

    private static float noise2D(float x, float y) {
        float n = (float) (Math.sin((double) x * 12.9898 + (double) y * 78.233) * 43758.5453);
        return n - (float) Math.floor(n);
    }

    private static float smoothNoise2D(float x, float y) {
        float i = (float) Math.floor(x);
        float j = (float) Math.floor(y);
        float fx = x - i;
        float fy = y - j;
        float sx = fx * fx * (3.0f - 2.0f * fx);
        float sy = fy * fy * (3.0f - 2.0f * fy);
        float n00 = ClientTooltipHandler.noise2D(i, j);
        float n10 = ClientTooltipHandler.noise2D(i + 1.0f, j);
        float n01 = ClientTooltipHandler.noise2D(i, j + 1.0f);
        float n11 = ClientTooltipHandler.noise2D(i + 1.0f, j + 1.0f);
        return n00 * (1.0f - sx) * (1.0f - sy) + n10 * sx * (1.0f - sy) + n01 * (1.0f - sx) * sy + n11 * sx * sy;
    }

    private static int getNoiseBlueWhiteOrangeColor(float x, float y) {
        double t = (double) System.currentTimeMillis() / 1000.0 * 0.6;
        float nx = x * 0.02f + (float) t * 0.6f;
        float ny = y * 0.02f - (float) t * 0.4f;
        float n1 = ClientTooltipHandler.smoothNoise2D(nx, ny);
        float n2 = ClientTooltipHandler.smoothNoise2D(nx * 1.7f, ny * 1.3f + 5.0f);
        float n = (n1 * 0.7f + n2 * 0.3f) * 1.3f;
        n = Math.max(0.0f, Math.min(1.0f, n));
        Color blue = new Color(30, 80, 220);
        Color white = new Color(220, 230, 255);
        Color orange = new Color(255, 140, 50);
        float t1 = n < 0.5f ? n * 2.0f : 1.0f;
        float t2 = n < 0.5f ? 0.0f : (n - 0.5f) * 2.0f;
        Color mid = new Color((int) ((float) blue.getRed() + t1 * (float) (white.getRed() - blue.getRed())),
                (int) ((float) blue.getGreen() + t1 * (float) (white.getGreen() - blue.getGreen())),
                (int) ((float) blue.getBlue() + t1 * (float) (white.getBlue() - blue.getBlue())));
        return new Color((int) ((float) mid.getRed() + t2 * (float) (orange.getRed() - mid.getRed())),
                (int) ((float) mid.getGreen() + t2 * (float) (orange.getGreen() - mid.getGreen())),
                (int) ((float) mid.getBlue() + t2 * (float) (orange.getBlue() - mid.getBlue()))).getRGB();
    }

    private static int getNoiseBlueWhiteOrangeBorderColor(float x, float y) {
        double t = (double) System.currentTimeMillis() / 1000.0 * 0.8;
        float nx = x * 0.015f + (float) t * 0.5f;
        float ny = y * 0.015f - (float) t * 0.3f;
        float n = ClientTooltipHandler.smoothNoise2D(nx, ny);
        n = Math.max(0.0f, Math.min(1.0f, n * 1.2f));
        Color sky = new Color(80, 180, 255);
        Color purple = new Color(200, 100, 255);
        return new Color((int) ((float) sky.getRed() + n * (float) (purple.getRed() - sky.getRed())),
                (int) ((float) sky.getGreen() + n * (float) (purple.getGreen() - sky.getGreen())),
                (int) ((float) sky.getBlue() + n * (float) (purple.getBlue() - sky.getBlue()))).getRGB();
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        boolean isOther;
        boolean isUnknownArmor;
        boolean isAlepbArmor;
        boolean isGoldenArmor;
        boolean isUpgradeArmor;
        boolean isTianyuanArmor;
        Component comp;
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }
        isHoveringKingsShadow = false;
        List tip = event.getToolTip();
        for (int i = 0; i < tip.size(); ++i) {
            Component comp2 = (Component) tip.get(i);
            String text = comp2.getString();
            if (!text.contains("enchantment.level."))
                continue;
            tip.set(i, ClientTooltipHandler.fixEnchantmentLevel(comp2));
        }
        if (stack.is((Item) ModItems.MANBO_VOID_SWORD.get())) {
            tip = event.getToolTip();
            ArrayList<Component> newTip = new ArrayList<Component>();
            for (int i = 0; i < tip.size(); ++i) {
                comp = (Component) tip.get(i);
                String text = comp.getString();
                if (i == 0) {
                    newTip.add(comp);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*") || text.matches("^\\s*$"))
                    continue;
                newTip.add(comp);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ManboVoidSword.createGlitchDamageText());
                newTip.add(ManboVoidSword.createMysterySpeedText());
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        if (stack.is((Item) ModItems.MANBOHAJIMI_SUPER_SWORD.get())) {
            tip = event.getToolTip();
            ArrayList<Component> newTip = new ArrayList<Component>();
            for (int i = 0; i < tip.size(); ++i) {
                comp = (Component) tip.get(i);
                String text = comp.getString();
                if (i == 0) {
                    newTip.add(comp);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*") || text.matches("^\\s*$"))
                    continue;
                newTip.add(comp);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ManbohajimiSuperSword.createMiracleDamageText());
                newTip.add(ManbohajimiSuperSword.createMysterySpeedText());
                newTip.add(ManbohajimiSuperSword.createDistanceText());
                newTip.add(ManbohajimiSuperSword.createDefenseText());
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        boolean bl = isTianyuanArmor = stack.is((Item) ModItems.TIANYUAN_HELMET.get())
                || stack.is((Item) ModItems.TIANYUAN_CHESTPLATE.get())
                || stack.is((Item) ModItems.TIANYUAN_LEGGINGS.get()) || stack.is((Item) ModItems.TIANYUAN_BOOTS.get());
        if (isTianyuanArmor) {
            tip = event.getToolTip();
            ArrayList<Object> newTip = new ArrayList<Object>();
            for (int i = 0; i < tip.size(); ++i) {
                Component comp3 = (Component) tip.get(i);
                String text = comp3.getString();
                if (i == 0) {
                    newTip.add(comp3);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*"))
                    continue;
                newTip.add(comp3);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ColorUtils.createWhiteBlueGradientText("  +\u5b89\u8c27\u4e61 \u62a4\u7532\u503c"));
                newTip.add(
                        ColorUtils.createWhiteBlueGradientText("  +\u8d85\u8d8a\u8fd0\u7b97 \u76d4\u7532\u97e7\u6027"));
                newTip.add(
                        ColorUtils.createWhiteBlueGradientText("  +\u5e1d\u738b\u4f20\u8bf4 \u51fb\u9000\u6297\u6027"));
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        boolean bl2 = isUpgradeArmor = stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_BOOTS.get());
        if (isUpgradeArmor) {
            tip = event.getToolTip();
            ArrayList<Object> newTip = new ArrayList<Object>();
            for (int i = 0; i < tip.size(); ++i) {
                Component comp4 = (Component) tip.get(i);
                String text = comp4.getString();
                if (i == 0) {
                    newTip.add(comp4);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*"))
                    continue;
                newTip.add(comp4);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ColorUtils.createBlackSilverGradientText("  +\u575a\u4e0d\u53ef\u6467 \u62a4\u7532\u503c"));
                newTip.add(ColorUtils
                        .createBlackSilverGradientText("  +\u96be\u4ee5\u4f30\u6d4b \u76d4\u7532\u97e7\u6027"));
                newTip.add(ColorUtils
                        .createBlackSilverGradientText("  +\u66fc\u6ce2\u610f\u5fd7 \u51fb\u9000\u6297\u6027"));
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        boolean bl3 = isGoldenArmor = stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_BOOTS.get());
        if (isGoldenArmor) {
            tip = event.getToolTip();
            ArrayList<Object> newTip = new ArrayList<Object>();
            for (int i = 0; i < tip.size(); ++i) {
                Component comp5 = (Component) tip.get(i);
                String text = comp5.getString();
                if (i == 0) {
                    newTip.add(comp5);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*"))
                    continue;
                newTip.add(comp5);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ColorUtils.createGoldSilverGradientText("  +\u9ec4\u91d1\u4f53\u9a8c \u62a4\u7532\u503c"));
                newTip.add(ColorUtils
                        .createGoldSilverGradientText("  +\u5915\u65e5\u4e4b\u68a6 \u76d4\u7532\u97e7\u6027"));
                newTip.add(ColorUtils.createGoldSilverGradientText("  +\u9547\u9b42\u66f2 \u51fb\u9000\u6297\u6027"));
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        boolean bl4 = isAlepbArmor = stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_BOOTS.get());
        if (isAlepbArmor) {
            tip = event.getToolTip();
            ArrayList<Object> newTip = new ArrayList<Object>();
            for (int i = 0; i < tip.size(); ++i) {
                Component comp6 = (Component) tip.get(i);
                String text = comp6.getString();
                if (i == 0) {
                    newTip.add(comp6);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*"))
                    continue;
                newTip.add(comp6);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ColorUtils.createSeaBlueSkyBlueGradientText("  +\u2135inf \u62a4\u7532\u503c"));
                newTip.add(ColorUtils.createSeaBlueSkyBlueGradientText("  +\u2135inf \u76d4\u7532\u97e7\u6027"));
                newTip.add(ColorUtils.createSeaBlueSkyBlueGradientText("  +\u2135inf \u51fb\u9000\u6297\u6027"));
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        boolean bl5 = isUnknownArmor = stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_BOOTS.get());
        if (isUnknownArmor) {
            tip = event.getToolTip();
            ArrayList<Object> newTip = new ArrayList<Object>();
            for (int i = 0; i < tip.size(); ++i) {
                Component comp7 = (Component) tip.get(i);
                String text = comp7.getString();
                if (i == 0) {
                    newTip.add(comp7);
                    continue;
                }
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*"))
                    continue;
                newTip.add(comp7);
                String trimmed = text.trim();
                if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                    continue;
                newTip.add(ColorUtils
                        .createPurplePinkBlueGreenYellowGradientText("  +\u7231\u610f\u8fd1\u77e3 \u62a4\u7532\u503c"));
                newTip.add(ColorUtils.createPurplePinkBlueGreenYellowGradientText(
                        "  +\u5fc3\u613f\u4e4b\u5904 \u76d4\u7532\u97e7\u6027"));
                newTip.add(ColorUtils.createPurplePinkBlueGreenYellowGradientText(
                        "  +\u7480\u74a8\u7ec8\u70b9 \u51fb\u9000\u6297\u6027"));
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            if (newTip.size() > 1) {
                tip.addAll(newTip.subList(1, newTip.size()));
            }
            return;
        }
        if (ClientTooltipHandler.isKingsShadow(stack)) {
            String[] statTexts;
            tip = event.getToolTip();
            cachedTitleText = tip.isEmpty() ? null : ((Component) tip.get(0)).getString();
            cachedFixedLines.clear();
            cachedNarrativeLines.clear();
            cachedNarrativeTypes.clear();
            cachedStatLines.clear();
            cachedEnchantTexts.clear();
            List<Component> fullTip = new ArrayList<Component>();
            if (!tip.isEmpty()) {
                fullTip.add(ClientTooltipHandler.createBluePurpleGradientText(((Component) tip.get(0)).getString()));
            }
            cachedEnchantTexts.clear();
            Map enchantMap = EnchantmentHelper.getEnchantments((ItemStack) stack);
            for (Object entryObj : enchantMap.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                Enchantment enchant = (Enchantment) entry.getKey();
                String baseName = Component.translatable((String) enchant.getDescriptionId()).getString();
                cachedEnchantTexts.add(baseName);
            }
            for (int i = 1; i < tip.size(); ++i) {
                String trimmed;
                Component comp8 = (Component) tip.get(i);
                String text = comp8.getString();
                if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*")
                        || (trimmed = text.trim()).startsWith("\u62a4\u7532\u503c")
                        || trimmed.startsWith("\u76d4\u7532\u97e7\u6027")
                        || trimmed.startsWith("\u51fb\u9000\u6297\u6027") || trimmed.startsWith("+\u661f\u5815")
                        || trimmed.startsWith("+\u8702\u871c\u6ee1\u76c8") || trimmed.startsWith("+\u7231\u4e4b\u773c")
                        || trimmed.startsWith("+\u661f\u5760\u6c38\u591c")
                        || trimmed.startsWith("+\u6697\u591c\u660e\u95ea")
                        || trimmed.startsWith("+\u6c38\u7231\u4e0d\u606f") || trimmed.isEmpty()
                        || trimmed.contains("\u65f6:") || trimmed.contains("\u65f6\uff1a")
                        || trimmed.contains("\u81ea\u52a8\u9644\u9b54"))
                    continue;
                boolean isEnchantLine = false;
                for (String enchantName : cachedEnchantTexts) {
                    if (!trimmed.startsWith(enchantName))
                        continue;
                    isEnchantLine = true;
                    break;
                }
                if (isEnchantLine)
                    continue;
                if (ClientTooltipHandler.isKingsShadowDarkGrayLine(trimmed)) {
                    fullTip.add((Component) Component.literal((String) text).withStyle(ChatFormatting.DARK_GRAY));
                    cachedNarrativeLines.add(text);
                    cachedNarrativeTypes.add(0);
                    continue;
                }
                if (ClientTooltipHandler.isKingsShadowRedOrangeBlueLine(trimmed)) {
                    fullTip.add((Component) ColorUtils.createRedOrangeBlueGradientText(text));
                    cachedNarrativeLines.add(text);
                    cachedNarrativeTypes.add(1);
                    continue;
                }
                fullTip.add(ClientTooltipHandler.createLightBlueToDarkBlueGradientText(text));
                cachedNarrativeLines.add(text);
                cachedNarrativeTypes.add(2);
            }
            for (String s : statTexts = new String[] { "  +\u661f\u5815 \u62a4\u7532\u503c",
                    "  +\u8702\u871c\u6ee1\u76c8 \u97e7\u6027", "  +\u7231\u4e4b\u773c \u51fb\u9000\u6297\u6027",
                    "  +\u661f\u5760\u6c38\u591c \u903b\u8f91\u4fdd\u62a4",
                    "  +\u6697\u591c\u660e\u95ea \u6297\u6050\u6016\u6027",
                    "  +\u6c38\u7231\u4e0d\u606f \u589e\u76ca\u6027" }) {
                fullTip.add(ClientTooltipHandler.createLightBlueToDarkBlueGradientText(s));
                cachedStatLines.add(s);
            }
            isHoveringKingsShadow = true;
            lastTooltipActiveTime = System.currentTimeMillis();
            currentKingsShadowStack = stack;
            fullTip = ClientTooltipHandler.buildPagedTooltip(fullTip, stack);
            cachedTooltipContent = new ArrayList<Component>(fullTip);
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            return;
        }
        if (stack.is((Item) ModItems.MANBO_FINAL_SWORD.get())) {
            tip = event.getToolTip();
            ArrayList<Object> finalTip = new ArrayList<Object>();
            String titleText = tip.isEmpty() ? stack.getHoverName().getString() : ((Component) tip.get(0)).getString();
            finalTip.add(ColorUtils.createBluePurpleGreenGradientText(titleText));
            String lore = Component.translatable((String) "tooltip.manbov2c.manbo_final_sword").getString();
            String[] allLines = lore.split("\n");
            finalSwordNarrativeLines.clear();
            finalSwordStatLines.clear();
            for (String line : allLines) {
                String trimmed = line.trim();
                if (trimmed.startsWith("\u65e0\u5c3d\u4e4b\u7231") || trimmed.startsWith("\u5b89\u8c27\u6c38\u6052")
                        || trimmed.startsWith("\u96ea\u6563\u5343\u91cc") || trimmed.startsWith("eieimun")) {
                    finalSwordStatLines.add(line);
                    continue;
                }
                finalSwordNarrativeLines.add(line);
            }
            int linesPerPage = 9;
            int nCount = finalSwordNarrativeLines.size();
            int sCount = finalSwordStatLines.size();
            if (nCount > linesPerPage) {
                finalSwordTotalPages = (int) Math.ceil((double) nCount / (double) linesPerPage);
                if (finalSwordPage >= finalSwordTotalPages) {
                    finalSwordPage = 0;
                }
                int start = finalSwordPage * linesPerPage;
                int end = Math.min(start + linesPerPage, nCount);
                for (int i = start; i < end; ++i) {
                    String l = finalSwordNarrativeLines.get(i);
                    if (!l.trim().isEmpty()) {
                        finalTip.add(ColorUtils.createBluePurpleGreenGradientText(l));
                        continue;
                    }
                    finalTip.add(Component.literal((String) ""));
                }
            } else {
                finalSwordTotalPages = 1;
                finalSwordPage = 0;
                for (String line : finalSwordNarrativeLines) {
                    if (!line.trim().isEmpty()) {
                        finalTip.add(ColorUtils.createBluePurpleGreenGradientText(line));
                        continue;
                    }
                    finalTip.add(Component.literal((String) ""));
                }
            }
            finalTip.add(Component.literal((String) ""));
            finalTip.add(ClientTooltipHandler.createBlackWhiteGradientText("\u63e1\u4e8e\u624b\u4e2d\u65f6\uff1a"));
            for (String s : finalSwordStatLines) {
                finalTip.add(ColorUtils.createBluePurpleGreenGradientText(s.trim()));
            }
            if (finalSwordTotalPages > 1) {
                finalTip.add(Component.literal((String) ""));
                finalTip.add(Component.literal((String) ("  \u00a78\u2014 \u7b2c " + (finalSwordPage + 1) + "/"
                        + finalSwordTotalPages + " \u9875 \u2014")).withStyle(ChatFormatting.GRAY));
            }
            if (tip.isEmpty()) {
                tip.add(stack.getHoverName());
            }
            tip.subList(1, tip.size()).clear();
            tip.addAll(finalTip.subList(1, finalTip.size()));
            tip.set(0, (Component) finalTip.get(0));
            isHoveringFinalSword = true;
            finalSwordLastActiveTime = System.currentTimeMillis();
            return;
        }
        isHoveringFinalSword = false;
        finalSwordPage = 0;
        finalSwordTotalPages = 1;
        finalSwordNarrativeLines.clear();
        finalSwordStatLines.clear();
        boolean isArmor = stack.is((Item) ModItems.SHUANGSHENG_HELMET.get())
                || stack.is((Item) ModItems.SHUANGSHENG_CHESTPLATE.get())
                || stack.is((Item) ModItems.SHUANGSHENG_LEGGINGS.get())
                || stack.is((Item) ModItems.SHUANGSHENG_BOOTS.get());
        boolean isSword = stack.is((Item) ModItems.SHUANGSHENG_SWORD.get());
        boolean bl6 = isOther = stack.is((Item) ModItems.SHUANGSHENG_MATI.get())
                || stack.is((Item) ModItems.SHUANGSHENG_CORE.get());
        if (!(isArmor || isSword || isOther)) {
            return;
        }
        tip = event.getToolTip();
        ArrayList<Component> newTip = new ArrayList<Component>();
        for (int i = 0; i < tip.size(); ++i) {
            Component comp9 = (Component) tip.get(i);
            String text = comp9.getString();
            if (i == 0) {
                newTip.add(comp9);
                continue;
            }
            if (text.matches("^\\s*[+-]?\\d+(\\.\\d+)?\\s.*"))
                continue;
            newTip.add(comp9);
            String trimmed = text.trim();
            if (!trimmed.endsWith("\u65f6:") && !trimmed.endsWith("\u65f6\uff1a"))
                continue;
            if (isSword) {
                newTip.add(ClientTooltipHandler
                        .createGoldSilverGradientText("  +\u60f3\u8c61\u57df\u9650 \u653b\u51fb\u4f24\u5bb3"));
                newTip.add(ClientTooltipHandler
                        .createGoldSilverGradientText("  +\u5929\u5143\u5316\u521d \u653b\u51fb\u901f\u5ea6"));
                continue;
            }
            newTip.add(ClientTooltipHandler
                    .createGoldSilverGradientText("  +\u9065\u8fdc\u661f\u8f89 \u62a4\u7532\u503c"));
            newTip.add(ClientTooltipHandler
                    .createGoldSilverGradientText("  +\u4e0d\u53ef\u601d\u8bae \u76d4\u7532\u97e7\u6027"));
            newTip.add(ClientTooltipHandler
                    .createGoldSilverGradientText("  +\u53cc\u751f\u4e4b\u5143 \u51fb\u9000\u6297\u6027"));
        }
        if (tip.isEmpty()) {
            tip.add(stack.getHoverName());
        }
        tip.subList(1, tip.size()).clear();
        if (newTip.size() > 1) {
            tip.addAll(newTip.subList(1, newTip.size()));
        }
    }

    private static boolean isKingsShadow(ItemStack stack) {
        return stack.is((Item) ModItems.KINGS_SHADOW.get());
    }

    private static boolean isKingsShadowDarkGrayLine(String trimmed) {
        return trimmed.equals("\u6211\u5df2\u7ecf\u5386\u8fc7\u597d\u51e0\u6b21\u632b\u6298\u4e86") || trimmed.equals(
                "\u90a3\u4e2a\u65f6\u5019\u4e5f\u662f\uff0c\u90a3\u65f6\u5019\u4e5f\u662f\uff0c\u6bd4\u4efb\u4f55\u4eba\u90fd\u8fd8\u53d7\u632b\u3002")
                || trimmed.equals(
                        "\u6bd4\u4efb\u4f55\u4eba\u90fd\u8fd8\u4e0d\u7518\u5fc3\u7684\u662f\u6211\uff0c\u6bd4\u4efb\u4f55\u4eba\u90fd\u60f3\u8981\u53d6\u80dc\u7684\u662f\u6211\u3002")
                || trimmed.equals(
                        "\u80dc\u5229\u7edd\u5bf9\u4e0d\u4f1a\u9000\u8ba9\uff0c\u80dc\u5229\u7684\u7edd\u5bf9...\u7edd\u5bf9\uff01\u7edd\u5bf9\u662f\u6211\uff01");
    }

    private static boolean isKingsShadowRedOrangeBlueLine(String trimmed) {
        return trimmed.equals("\u4e00\u51b3\u80dc\u8d1f\u5427\uff01") || trimmed.equals(
                "\u4e0d\u8d25\u7684\u5929\u624d\u5df2\u7136\u901d\u53bb\uff0c\u4e0d\u5c48\u7684\u5e1d\u738b\u5c31\u6b64\u8bde\u751f\u3002");
    }

    private static List<Component> buildPagedTooltip(List<Component> fullTip, ItemStack stack) {
        int i;
        int fixedCount = cachedFixedLines != null ? cachedFixedLines.size() : 0;
        int narrativeCount = cachedNarrativeLines != null ? cachedNarrativeLines.size() : 0;
        int statCount = cachedStatLines != null ? cachedStatLines.size() : 0;
        int linesPerPage = 9;
        boolean sameItem = ClientTooltipHandler.isCurrentStackKingsShadow(stack);
        if (!sameItem) {
            tooltipPage = 0;
        }
        currentKingsShadowStack = stack;
        ArrayList<Component> pagedTip = new ArrayList<Component>();
        pagedTip.add(fullTip.get(0));
        for (i = 1; i <= fixedCount; ++i) {
            pagedTip.add(fullTip.get(i));
        }
        if (narrativeCount > linesPerPage) {
            totalPages = (int) Math.ceil((double) narrativeCount / (double) linesPerPage);
            if (tooltipPage >= totalPages) {
                tooltipPage = 0;
            }
            int startIdx = tooltipPage * linesPerPage;
            int endIdx = Math.min(startIdx + linesPerPage, narrativeCount);
            for (int i2 = startIdx; i2 < endIdx; ++i2) {
                pagedTip.add(fullTip.get(1 + fixedCount + i2));
            }
        } else {
            totalPages = 1;
            for (i = 0; i < narrativeCount; ++i) {
                pagedTip.add(fullTip.get(1 + fixedCount + i));
            }
        }
        if (!cachedEnchantTexts.isEmpty() || statCount > 0) {
            pagedTip.add((Component) Component.literal((String) "").withStyle(ChatFormatting.DARK_GRAY));
        }
        pagedTip.add(ClientTooltipHandler.createBlackWhiteGradientText("\u4e0e\u4f60\u5171\u5728\u65f6\uff1a"));
        for (String enchantName : cachedEnchantTexts) {
            pagedTip.add(ClientTooltipHandler.createBluePurpleGradientText(enchantName + " \u8d85\u8d8a\u73b0\u5b9e"));
        }
        for (int i3 = 0; i3 < statCount; ++i3) {
            pagedTip.add(fullTip.get(1 + fixedCount + narrativeCount + i3));
        }
        if (totalPages > 1) {
            pagedTip.add((Component) Component.literal((String) "").withStyle(ChatFormatting.DARK_GRAY));
            pagedTip.add((Component) Component.literal(
                    (String) ("  \u00a78\u2014 \u7b2c " + (tooltipPage + 1) + "/" + totalPages + " \u9875 \u2014"))
                    .withStyle(ChatFormatting.GRAY));
        }
        return pagedTip;
    }

    private static void rebuildTooltipFromCache() {
        if (cachedTitleText == null) {
            return;
        }
        ArrayList<Component> fullTip = new ArrayList<Component>();
        fullTip.add(ClientTooltipHandler.createBluePurpleGradientText(cachedTitleText));
        for (String s : cachedFixedLines) {
            fullTip.add(ClientTooltipHandler.createLightBlueToDarkBlueGradientText(s));
        }
        for (int i = 0; i < cachedNarrativeLines.size(); ++i) {
            int type;
            String s;
            s = cachedNarrativeLines.get(i);
            int n = type = i < cachedNarrativeTypes.size() ? cachedNarrativeTypes.get(i) : 0;
            if (type == 0) {
                fullTip.add((Component) Component.literal((String) s).withStyle(ChatFormatting.DARK_GRAY));
                continue;
            }
            if (type == 1) {
                fullTip.add((Component) ColorUtils.createRedOrangeBlueGradientText(s));
                continue;
            }
            fullTip.add(ClientTooltipHandler.createLightBlueToDarkBlueGradientText(s));
        }
        for (String s : cachedStatLines) {
            fullTip.add(ClientTooltipHandler.createLightBlueToDarkBlueGradientText(s));
        }
        cachedTooltipContent = ClientTooltipHandler.buildPagedTooltip(fullTip, currentKingsShadowStack);
    }

    private static Component createHoneyFleshGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 0.8;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float ratio = (float) (((double) i / (double) (len - 1) + timeOffset) % 1.0);
            int r = (int) (240.0f * (1.0f - ratio) + 255.0f * ratio);
            int g = (int) (192.0f * (1.0f - ratio) + 218.0f * ratio);
            int b = (int) (64.0f * (1.0f - ratio) + 185.0f * ratio);
            int color = r << 16 | g << 8 | b;
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }

    private static int getDynamicDeepCyanColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.2;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color lightPink = new Color(255, 182, 193);
        Color palePink = new Color(255, 192, 203);
        int r = (int) ((float) lightPink.getRed() + factor * (float) (palePink.getRed() - lightPink.getRed()));
        int g = (int) ((float) lightPink.getGreen() + factor * (float) (palePink.getGreen() - lightPink.getGreen()));
        int b = (int) ((float) lightPink.getBlue() + factor * (float) (palePink.getBlue() - lightPink.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicDarkBlueOrangeSineColor(float x, float y, float screenW, float screenH) {
        float dx = (screenW - x) / screenW;
        float dy = (screenH - y) / screenH;
        float dist = (dx + dy) * 0.5f;
        double time = (double) System.currentTimeMillis() / 1000.0;
        float factor = (float) (Math.sin(time * 2.0 + (double) dist * Math.PI * 4.0) * 0.5 + 0.5);
        Color darkBlue = new Color(0, 0, 139);
        Color orange = new Color(255, 140, 0);
        int r = (int) ((float) darkBlue.getRed() + factor * (float) (orange.getRed() - darkBlue.getRed()));
        int g = (int) ((float) darkBlue.getGreen() + factor * (float) (orange.getGreen() - darkBlue.getGreen()));
        int b = (int) ((float) darkBlue.getBlue() + factor * (float) (orange.getBlue() - darkBlue.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int hsvToRgb(float hue, float saturation, float value) {
        float g = 0;
        float r = 0;
        int h = (int) (hue * 6.0f);
        float f = hue * 6.0f - (float) h;
        float p = value * (1.0f - saturation);
        float q = value * (1.0f - f * saturation);
        float t = value * (1.0f - (1.0f - f) * saturation);
        return (int) (r * 255.0f) << 16 | (int) (g * 255.0f) << 8 | (int) ((switch (h % 6) {
            case 0 -> {
                r = value;
                g = t;
                yield p;
            }
            case 1 -> {
                r = q;
                g = value;
                yield p;
            }
            case 2 -> {
                r = p;
                g = value;
                yield t;
            }
            case 3 -> {
                r = p;
                g = q;
                yield value;
            }
            case 4 -> {
                r = t;
                g = p;
                yield value;
            }
            case 5 -> {
                r = value;
                g = p;
                yield q;
            }
            default -> {
                r = value;
                g = p;
                yield q;
            }
        }) * 255.0f);
    }

    private static void renderRainbowRing(GuiGraphics guiGraphics, int x, int y, int w, int h) {
        float time = (float) (System.currentTimeMillis() % 8000L) / 8000.0f;
        int thickness = 4;
        int spacing = 3;
        int margin = 2;
        int ox = x - margin - thickness;
        int oy = y - margin - thickness;
        int ow = w + 2 * (margin + thickness);
        int oh = h + 2 * (margin + thickness);
        int perimeter = 2 * (ow + oh);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 399.0f);
        for (int p = 0; p < perimeter; p += spacing) {
            int rh;
            int rw;
            int ry;
            int rx;
            float hue = (time + (float) p / (float) perimeter) % 1.0f;
            int color = ClientTooltipHandler.hsvToRgb(hue, 0.8f, 1.0f) | 0xFF000000;
            int p2 = 0;
            if (p < ow) {
                rx = ox + p;
                ry = oy;
                rw = spacing;
                rh = thickness;
            } else if (p < ow + oh) {
                p2 = p - ow;
                rx = ox + ow - thickness;
                ry = oy + p2;
                rw = thickness;
                rh = spacing;
            } else if (p < ow + oh + ow) {
                p2 = p - ow - oh;
                rx = ox + ow - p2 - spacing;
                ry = oy + oh - thickness;
                rw = spacing;
                rh = thickness;
            } else {
                p2 = p - ow - oh - ow;
                rx = ox;
                ry = oy + oh - p2 - spacing;
                rw = thickness;
                rh = spacing;
            }
            rw = Math.min(rw, ox + ow - rx);
            rh = Math.min(rh, oy + oh - ry);
            if (rw <= 0 || rh <= 0)
                continue;
            guiGraphics.fill(rx, ry, rx + rw, ry + rh, color);
        }
        guiGraphics.pose().popPose();
    }

    private static Component createGoldSilverGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 0.8;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float ratio = (float) (((double) i / (double) (len - 1) + timeOffset) % 1.0);
            int r = (int) (255.0f * (1.0f - ratio) + 192.0f * ratio);
            int g = (int) (215.0f * (1.0f - ratio) + 192.0f * ratio);
            int b = (int) (0.0f * (1.0f - ratio) + 192.0f * ratio);
            int color = r << 16 | g << 8 | b;
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }

    private static Component createGoldBlueGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.2;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float ratio = (float) ((Math.cos(timeOffset + (double) i * 0.25) + 1.0) / 2.0);
            int r = (int) (255.0f * (1.0f - ratio) + 65.0f * ratio);
            int g = (int) (215.0f * (1.0f - ratio) + 105.0f * ratio);
            int b = (int) (0.0f * (1.0f - ratio) + 225.0f * ratio);
            int color = r << 16 | g << 8 | b;
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }

    private static Component createLightBlueToDarkBlueGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.2;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float ratio = (float) ((Math.cos(timeOffset + (double) i * 0.25) + 1.0) / 2.0);
            int r = (int) (135.0f * (1.0f - ratio) + 0.0f * ratio);
            int g = (int) (206.0f * (1.0f - ratio) + 0.0f * ratio);
            int b = (int) (235.0f * (1.0f - ratio) + 139.0f * ratio);
            int color = r << 16 | g << 8 | b;
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }

    private static Component createBlackWhiteGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.5;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            double seed = timeOffset + (double) i * 1.7;
            float gray = (float) ((Math.sin(seed) * 0.5 + 0.5) * 0.6 + (Math.cos(seed * 0.7) * 0.5 + 0.5) * 0.4);
            int c = Math.max(0, Math.min(255, (int) (gray * 255.0f)));
            int color = c << 16 | c << 8 | c;
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }

    private static Component createBluePurpleGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 2.5;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float ratio = (float) ((Math.cos(timeOffset + (double) i * 0.3) + 1.0) / 2.0);
            int r = (int) (74.0f * (1.0f - ratio) + 155.0f * ratio);
            int g = (int) (144.0f * (1.0f - ratio) + 89.0f * ratio);
            int b = (int) (217.0f * (1.0f - ratio) + 182.0f * ratio);
            int color = r << 16 | g << 8 | b;
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }

    private static int getDynamicPurpleBlackColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.0;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(40, 0, 80);
        Color color2 = new Color(5, 0, 15);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicPurplePinkColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.4;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(140, 0, 255);
        Color color2 = new Color(255, 0, 180);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static int getDynamicDeepBlueLightPinkColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.2;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(10, 20, 80);
        Color color2 = new Color(255, 180, 220);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }
        if (stack.is((Item) ModItems.MANBO_VOID_SWORD.get())) {
            int bg1 = ClientTooltipHandler.getDynamicPurpleBlackColor(0.0f);
            int bg2 = ClientTooltipHandler.getDynamicPurpleBlackColor(1.5f);
            int border1 = ClientTooltipHandler.getDynamicPurplePinkColor(0.0f);
            int border2 = ClientTooltipHandler.getDynamicPurplePinkColor(1.5f);
            event.setBackgroundStart(bg1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
            return;
        }
        if (ClientTooltipHandler.isTianyuanItem(stack)) {
            int bg1 = ColorUtils.getDynamicWhiteBlueColor(0.0f);
            int bg2 = ColorUtils.getDynamicWhiteBlueColor(2.0f);
            int border1 = ColorUtils.getDynamicWhiteBlueColor(1.0f);
            int border2 = ColorUtils.getDynamicWhiteBlueColor(3.0f);
            event.setBackgroundStart(bg1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
            return;
        }
        if (stack.is((Item) ModItems.MANBOHAJIMI_SUPER_SWORD.get())
                || stack.is((Item) ModItems.MANBO_PICKAXE_UPGRADE.get())) {
            int bg1 = ColorUtils.getDynamicOrangeBlueColor(0.0f);
            int bg2 = ColorUtils.getDynamicOrangeBlueColor((float) Math.PI);
            int border1 = ClientTooltipHandler.getDynamicRainbowColor(0.0f);
            int border2 = ClientTooltipHandler.getDynamicRainbowColor(0.5f);
            event.setBackgroundStart(bg1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
            return;
        }
        if (stack.is((Item) ModItems.MANBO_BASIC_SWORD.get()) || stack.is((Item) ModItems.MANBO_PICKAXE_BEGIN.get())) {
            int c1 = ClientTooltipHandler.getDynamicRainbowColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicRainbowColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(c2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_FINAL_SWORD.get())) {
            Minecraft mc = Minecraft.getInstance();
            int screenW = mc.getWindow().getGuiScaledWidth();
            int screenH = mc.getWindow().getGuiScaledHeight();
            int bg1 = ClientTooltipHandler.getDynamicDarkBlueOrangeSineColor(event.getX(), event.getY(), screenW,
                    screenH);
            int bg2 = ClientTooltipHandler.getDynamicDarkBlueOrangeSineColor(event.getX() + 10, event.getY() + 10,
                    screenW, screenH);
            int border1 = ClientTooltipHandler.getDynamicDarkBlueOrangeSineColor(event.getX(), event.getY(), screenW,
                    screenH);
            int border2 = ClientTooltipHandler.getDynamicDarkBlueOrangeSineColor(event.getX() + 15, event.getY() + 15,
                    screenW, screenH);
            event.setBackgroundStart(bg1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(border1 & 0xFFFFFF | 0xFF000000);
            event.setBorderEnd(border2 & 0xFFFFFF | 0xFF000000);
        } else if (stack.is((Item) ModItems.KINGS_SHADOW.get())
                || stack.isEmpty() && currentKingsShadowStack != null && !currentKingsShadowStack.isEmpty()
                        && cachedTooltipContent != null && !cachedTooltipContent.isEmpty()) {
            int bg1 = ClientTooltipHandler.getDynamicDeepCyanColor(0.0f);
            int bg2 = ClientTooltipHandler.getDynamicDeepCyanColor(2.0f);
            int border1 = ClientTooltipHandler.getDynamicDeepCyanColor(1.0f);
            int border2 = ClientTooltipHandler.getDynamicDeepCyanColor(3.0f);
            event.setBackgroundStart(bg1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_SWORD.get()) || stack.is((Item) ModItems.MANBO_PICKAXE_SUPER.get())) {
            int c1 = ClientTooltipHandler.getDynamicYellowGreenColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicYellowGreenColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(c2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.PIGGOD.get())) {
            int c1 = ClientTooltipHandler.getDynamicRedGreenColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicRedGreenColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(c2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_VOID_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicRedPurpleColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicRedPurpleColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(c2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_BEYOND_CORE.get())) {
            int c1 = ColorUtils.getDynamicCyanGreenColor(0.0f);
            int c2 = ColorUtils.getDynamicCyanGreenColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(c2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_INVISABLE_CORE.get())) {
            int c1 = ColorUtils.getDynamicRedPurpleColor2(0.0f);
            int c2 = ColorUtils.getDynamicRedPurpleColor2(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_INFINITY_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicRedOrangeColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicRedOrangeColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_UPGRADE_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicBlueWhiteColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicBlueWhiteColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(c2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.HAJIMI_HELMET.get()) || stack.is((Item) ModItems.HAJIMI_CHESTPLATE.get())
                || stack.is((Item) ModItems.HAJIMI_LEGGINGS.get()) || stack.is((Item) ModItems.HAJIMI_BOOTS.get())) {
            int border1 = ClientTooltipHandler.getDynamicOrangeCyanColor(0.0f);
            int border2 = ClientTooltipHandler.getDynamicOrangeCyanColor(0.5f);
            int bg1 = ColorUtils.getDynamicBluePinkColor(0.0f);
            int bg2 = ColorUtils.getDynamicBluePinkColor(0.5f);
            event.setBackgroundStart(bg1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.NANBEILVDOU_HELMET.get())
                || stack.is((Item) ModItems.NANBEILVDOU_CHESTPLATE.get())
                || stack.is((Item) ModItems.NANBEILVDOU_LEGGINGS.get())
                || stack.is((Item) ModItems.NANBEILVDOU_BOOTS.get())
                || stack.is((Item) ModItems.NANBEILVDOU_CORE.get())) {
            int border1 = ClientTooltipHandler.getDynamicGreenPurpleColor(0.0f);
            int border2 = ClientTooltipHandler.getDynamicGreenPurpleColor(0.5f);
            int bg1 = ColorUtils.getDynamicBluePinkColor(0.0f);
            int bg2 = ColorUtils.getDynamicBluePinkColor(0.5f);
            event.setBackgroundStart(bg1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.SHUANGSHENG_SWORD.get()) || stack.is((Item) ModItems.SHUANGSHENG_MATI.get())
                || stack.is((Item) ModItems.SHUANGSHENG_CORE.get())
                || stack.is((Item) ModItems.SHUANGSHENG_HELMET.get())
                || stack.is((Item) ModItems.SHUANGSHENG_CHESTPLATE.get())
                || stack.is((Item) ModItems.SHUANGSHENG_LEGGINGS.get())
                || stack.is((Item) ModItems.SHUANGSHENG_BOOTS.get())) {
            int border1 = ClientTooltipHandler.getDynamicRainbowColor(0.0f);
            int border2 = ClientTooltipHandler.getDynamicRainbowColor(0.5f);
            Minecraft mc = Minecraft.getInstance();
            int screenW = mc.getWindow().getGuiScaledWidth();
            int screenH = mc.getWindow().getGuiScaledHeight();
            int bg1 = ColorUtils.getDynamicOrangeBlueSineColor(event.getX(), event.getY(), screenW, screenH);
            int bg2 = ColorUtils.getDynamicOrangeBlueSineColor(event.getX() + 10, event.getY() + 10, screenW, screenH);
            event.setBackgroundStart(bg1 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBackgroundEnd(bg2 & 0xFFFFFF | Integer.MIN_VALUE);
            event.setBorderStart(border1 | 0xFF000000);
            event.setBorderEnd(border2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.UNIVERSE_SHARD.get())) {
            int c1 = ClientTooltipHandler.getDynamicBlackOrangeColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicBlackOrangeColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_UPGRADE_INFINITY_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicBlackSilverColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicBlackSilverColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_GOLDENHOUR_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicGoldShinyColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicGoldShinyColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_ALEPB_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicSeaBlueSkyBlueColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicSeaBlueSkyBlueColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_UNKNOWN_CORE.get())) {
            int c1 = ClientTooltipHandler.getDynamicBlackWhiteColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicBlackWhiteColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UPGRADE_BOOTS.get())) {
            int c1 = ClientTooltipHandler.getDynamicBlackSilverColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicBlackSilverColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_GOLDEN_BOOTS.get())) {
            int c1 = ClientTooltipHandler.getDynamicGoldShinyColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicGoldShinyColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_BOOTS.get())) {
            int c1 = ClientTooltipHandler.getDynamicSeaBlueSkyBlueColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicSeaBlueSkyBlueColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_UNKNOWN_BOOTS.get())) {
            int c1 = ClientTooltipHandler.getDynamicBlackWhiteColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicBlackWhiteColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        } else if (stack.is((Item) ModItems.ZERO.get()) || stack.is((Item) ModItems.FINAL_MANBO.get())
                || stack.is((Item) ModItems.FINAL_HAJIMI.get())) {
            int c1 = ClientTooltipHandler.getDynamicPinkOrangeColor(0.0f);
            int c2 = ClientTooltipHandler.getDynamicPinkOrangeColor(0.5f);
            event.setBackgroundStart(c1 & 0xFFFFFF | 0xD0000000);
            event.setBackgroundEnd(c2 & 0xFFFFFF | 0xD0000000);
            event.setBorderStart(c1 | 0xFF000000);
            event.setBorderEnd(c2 | 0xFF000000);
        }
    }

    private static int getDynamicPinkOrangeColor(float offset) {
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.4;
        float factor = (float) ((Math.cos(timeOffset + (double) offset) + 1.0) / 2.0);
        Color color1 = new Color(15884945);
        Color color2 = new Color(16231190);
        int r = (int) ((float) color1.getRed() + factor * (float) (color2.getRed() - color1.getRed()));
        int g = (int) ((float) color1.getGreen() + factor * (float) (color2.getGreen() - color1.getGreen()));
        int b = (int) ((float) color1.getBlue() + factor * (float) (color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    private static Component fixEnchantmentLevel(Component comp) {
        int numIdx;
        String text = comp.getString();
        int idx = text.indexOf("enchantment.level.");
        if (idx < 0) {
            return comp;
        }
        String prefix = text.substring(0, idx);
        String suffix = text.substring(idx);
        StringBuilder numStr = new StringBuilder();
        for (numIdx = "enchantment.level.".length(); numIdx < suffix.length()
                && Character.isDigit(suffix.charAt(numIdx)); ++numIdx) {
            numStr.append(suffix.charAt(numIdx));
        }
        if (numStr.length() == 0) {
            return comp;
        }
        int level = Integer.parseInt(numStr.toString());
        String roman = ColorUtils.toRoman(level);
        String trailing = suffix.substring(numIdx);
        return Component.literal((String) (prefix + roman + trailing)).withStyle(comp.getStyle());
    }

    private static boolean isTianyuanItem(ItemStack stack) {
        return stack.is((Item) ModItems.HAJIMI_TIANYUAN_ORB.get()) || stack.is((Item) ModItems.HAJIMI_TIANYUAN_UN.get())
                || stack.is((Item) ModItems.TIANYUAN_CORE_JUEX.get())
                || stack.is((Item) ModItems.DATIAN_CORE_JUEX.get()) || stack.is((Item) ModItems.HAJIMI_FEET.get())
                || stack.is((Item) ModBlocks.TIANYUAN_BLOCK_UN_ITEM.get())
                || stack.is((Item) ModBlocks.TIANYUAN_BLOCK_ITEM.get())
                || stack.is((Item) ModItems.TIANYUAN_HELMET.get())
                || stack.is((Item) ModItems.TIANYUAN_CHESTPLATE.get())
                || stack.is((Item) ModItems.TIANYUAN_LEGGINGS.get()) || stack.is((Item) ModItems.TIANYUAN_BOOTS.get());
    }

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        if (stack.is((Item) ModItems.MANBO_FINAL_SWORD.get())) {
            tooltipX = event.getX();
            tooltipY = event.getY();
            tooltipWidth = event.getComponents().stream().mapToInt(c -> c.getWidth(event.getFont())).max().orElse(0)
                    + 12;
            tooltipHeight = event.getComponents().stream().mapToInt(c -> c.getHeight()).sum() + 8;
            return;
        }
        if (!stack.is((Item) ModItems.KINGS_SHADOW.get())) {
            return;
        }
        tooltipX = event.getX();
        tooltipY = event.getY();
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
        boolean mouseOnTooltip;
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null || mc.font == null) {
            return;
        }
        if (isHoveringFinalSword && System.currentTimeMillis() - finalSwordLastActiveTime > 500L) {
            isHoveringFinalSword = false;
        }
        double mouseX = mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth()
                / (double) mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight()
                / (double) mc.getWindow().getScreenHeight();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        boolean onItem = isHoveringKingsShadow;
        boolean bl = mouseOnTooltip = cachedTooltipContent != null && mouseX >= (double) customTooltipX
                && mouseX <= (double) (customTooltipX + customTooltipWidth) && mouseY >= (double) customTooltipY
                && mouseY <= (double) (customTooltipY + customTooltipHeight);
        if (onItem) {
            persistenceActive = true;
            lastTooltipActiveTime = System.currentTimeMillis();
            ClientTooltipHandler.renderCustomTooltip(event, mouseX, mouseY, screenW, screenH, true);
        } else if (mouseOnTooltip && persistenceActive && System.currentTimeMillis() - lastTooltipActiveTime < 3000L) {
            ClientTooltipHandler.renderCustomTooltip(event, mouseX, mouseY, screenW, screenH, false);
        } else {
            persistenceActive = false;
            ClientTooltipHandler.closeKingsShadowTooltip();
        }
    }

    private static void renderCustomTooltip(ScreenEvent.Render.Post event, double mouseX, double mouseY, int screenW,
            int screenH, boolean onItem) {
        int posY;
        int posX;
        Minecraft mc = Minecraft.getInstance();
        ClientTooltipHandler.rebuildTooltipFromCache();
        if (cachedTooltipContent == null || cachedTooltipContent.isEmpty()) {
            return;
        }
        int padX = 6;
        int padY = 4;
        int maxTextWidth = cachedTooltipContent.stream().mapToInt(c -> mc.font.width((FormattedText) c)).max()
                .orElse(0);
        int totalLineHeight = 0;
        for (Component c2 : cachedTooltipContent) {
            int n;
            if (c2.getString().isEmpty()) {
                Objects.requireNonNull(mc.font);
                n = 9 / 2;
            } else {
                Objects.requireNonNull(mc.font);
                n = 9;
            }
            totalLineHeight += n;
        }
        int bgWidth = maxTextWidth + padX * 2;
        int bgHeight = totalLineHeight + padY * 2;
        if (!onItem && customTooltipWidth > 0) {
            posX = customTooltipX;
            posY = customTooltipY;
        } else if (tooltipX != 0 && tooltipY != 0) {
            posX = tooltipX;
            posY = tooltipY;
        } else {
            posX = (int) mouseX + 12;
            posY = (int) mouseY - 12;
        }
        if (posX + bgWidth > screenW - 5) {
            posX = screenW - bgWidth - 5;
        }
        if (posY + bgHeight > screenH - 5) {
            posY = screenH - bgHeight - 5;
        }
        if (posY < 5) {
            posY = 5;
        }
        if (posX < 5) {
            posX = 5;
        }
        GuiGraphics guiGraphics = event.getGuiGraphics();
        KingsShadowHexagramRenderer.renderHexagramAt(posX, posY, bgWidth, bgHeight);
        ClientTooltipHandler.renderRainbowRing(guiGraphics, posX, posY, bgWidth, bgHeight);
        int bgColor1 = ClientTooltipHandler.getDynamicDeepCyanColor(0.0f);
        int bgColor2 = ClientTooltipHandler.getDynamicDeepCyanColor(2.0f);
        int borderColor1 = ClientTooltipHandler.getDynamicDeepCyanColor(1.0f);
        int borderColor2 = ClientTooltipHandler.getDynamicDeepCyanColor(3.0f);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 400.0f);
        guiGraphics.fillGradient(posX, posY, posX + bgWidth, posY + bgHeight, bgColor1 & 0xFFFFFF | 0xD0000000,
                bgColor2 & 0xFFFFFF | 0xD0000000);
        int borderA = -16777216;
        guiGraphics.hLine(posX, posX + bgWidth - 1, posY, borderColor1 | borderA);
        guiGraphics.hLine(posX, posX + bgWidth - 1, posY + bgHeight - 1, borderColor2 | borderA);
        guiGraphics.vLine(posX, posY, posY + bgHeight - 1, borderColor1 | borderA);
        guiGraphics.vLine(posX + bgWidth - 1, posY, posY + bgHeight - 1, borderColor2 | borderA);
        guiGraphics.pose().popPose();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 401.0f);
        int textY = posY + padY;
        for (Component comp : cachedTooltipContent) {
            int n;
            guiGraphics.drawString(mc.font, comp, posX + padX, textY, -1, false);
            if (comp.getString().isEmpty()) {
                Objects.requireNonNull(mc.font);
                n = 9 / 2;
            } else {
                Objects.requireNonNull(mc.font);
                n = 9;
            }
            textY += n;
        }
        guiGraphics.pose().popPose();
        customTooltipX = posX;
        customTooltipY = posY;
        customTooltipWidth = bgWidth;
        customTooltipHeight = bgHeight;
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if (event.getKey() == 71) {
            if (isHoveringKingsShadow || persistenceActive) {
                ClientTooltipHandler.closeKingsShadowTooltip();
            }
            if (isHoveringFinalSword) {
                isHoveringFinalSword = false;
                finalSwordPage = 0;
                finalSwordTotalPages = 1;
                finalSwordNarrativeLines.clear();
                finalSwordStatLines.clear();
            }
        }
    }

    private static void closeKingsShadowTooltip() {
        isHoveringKingsShadow = false;
        persistenceActive = false;
        cachedTooltipContent = null;
        cachedTitleText = null;
        cachedFixedLines.clear();
        cachedNarrativeLines.clear();
        cachedNarrativeTypes.clear();
        cachedStatLines.clear();
        cachedEnchantTexts.clear();
        customTooltipX = 0;
        customTooltipY = 0;
        customTooltipWidth = 0;
        customTooltipHeight = 0;
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        boolean hitTooltip;
        if (event.getAction() != 1) {
            return;
        }
        if (event.getButton() != 0) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastClickTime < 300L) {
            return;
        }
        lastClickTime = now;
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            return;
        }
        double mouseX = mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth()
                / (double) mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight()
                / (double) mc.getWindow().getScreenHeight();
        if (isHoveringFinalSword && finalSwordTotalPages > 1) {
            boolean bl = hitTooltip = mouseX >= (double) tooltipX && mouseX <= (double) (tooltipX + tooltipWidth)
                    && mouseY >= (double) tooltipY && mouseY <= (double) (tooltipY + tooltipHeight);
            if (hitTooltip) {
                event.setCanceled(true);
                finalSwordPage = (finalSwordPage + 1) % finalSwordTotalPages;
                return;
            }
        }
        if (cachedTooltipContent == null || cachedTooltipContent.isEmpty()) {
            return;
        }
        boolean bl = hitTooltip = mouseX >= (double) customTooltipX
                && mouseX <= (double) (customTooltipX + customTooltipWidth) && mouseY >= (double) customTooltipY
                && mouseY <= (double) (customTooltipY + customTooltipHeight);
        if (hitTooltip) {
            event.setCanceled(true);
            if (totalPages > 1) {
                tooltipPage = (tooltipPage + 1) % totalPages;
                ClientTooltipHandler.rebuildTooltipFromCache();
            }
        }
    }
}
