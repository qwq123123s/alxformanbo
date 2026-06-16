/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 */
package com.manbo.v2c.util;

import java.awt.Color;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class ColorUtils {
    private static final char[] GLITCH_CHARS = new char[]{'\u2591', '\u2592', '\u2593', '\u2588', '\u2584', '\u2580', '\u258c', '\u2590', '\u25a0', '\u25a1', '\u25a2', '\u25aa', '\u25ab', '\u25a5', '\u25a6', '\u25a7', '\u25a8', '\u25a9', '\u25ad', '\u25af', '\u25cb', '\u25cf', '\u25d0', '\u25d1', '\u25d2', '\u25d3', '\u25d4', '\u25d5', '\u25d6', '\u25d7', '\u25d8', '\u25d9', '\u25da', '\u25db', '\u25dc', '\u25dd', '\u25de', '\u25df', '\u25e0', '\u25e1', '\u25e2', '\u25e3', '\u25e4', '\u25e5', '\u25e6', '\u25e7', '\u25e8', '\u25e9', '\u25ea', '\u25eb', '\u25ec', '\u25ed', '\u25ee', '\u25ef', '\u2758', '\u2759', '\u275a', '\u2771', '\u2772', '\u2773', '\u2774', '\u2775', '\u2776', '\u2777', '\u2778', '\u2779', '\u277a', '\u277b', '\u277c', '\u277e', '\u277f', '\u24c0', '\u24c1', '\u24c2', '\u24c3', '\u24c4', '\u24c5', '\u24c6', '\u24c7', '\u24c8', '\u24c9', '\u24ca', '\u24cb', '\u24cc', '\u24cd', '\u24ce', '\u24cf', '\u24d0', '\u24d1', '\u24d2', '\u24d3', '\u24d4', '\u24d5', '\u24d6', '\u24d7', '\u24d8', '\u24d9', '\u24da', '\u24db', '\u24dc', '\u24dd', '\u24de', '\u24df', '\u24e0', '\u24e1', '\u24e2', '\u24e3', '\u24e4', '\u24e5', '\u24e6', '\u24e7', '\u24e8', '\u24e9'};

    public static MutableComponent createRainbowText(String text, float speed, float spread) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        for (int i = 0; i < text.length(); ++i) {
            float hue = (float)((timeOffset + (double)((float)i * spread)) % 1.0);
            int color = Color.HSBtoRGB(hue, 0.85f, 1.0f);
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createGoldBorderWhiteCenterText(String text, float speed, float spread) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        Color gold = new Color(255, 215, 0);
        Color white = new Color(255, 255, 255);
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double)((float)i * spread);
            float factor = (float)((Math.cos(angle) + 1.0) / 2.0);
            int r = (int)((float)gold.getRed() + factor * (float)(white.getRed() - gold.getRed()));
            int g = (int)((float)gold.getGreen() + factor * (float)(white.getGreen() - gold.getGreen()));
            int b = (int)((float)gold.getBlue() + factor * (float)(white.getBlue() - gold.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createDualColorGradient(String text, Color color1, Color color2, float speed, float spread) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double)((float)i * spread);
            float factor = (float)((Math.cos(angle) + 1.0) / 2.0);
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createDualColorGradient(String text, Color color1, Color color2, float speed, float spread, float extraOffset) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double)((float)i * spread) + (double)extraOffset;
            float factor = (float)((Math.cos(angle) + 1.0) / 2.0);
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createBlueWhiteGradientText(String text) {
        Color blue = new Color(0, 0, 255);
        Color white = new Color(255, 255, 255);
        return ColorUtils.createDualColorGradient(text, blue, white, 0.8f, 0.05f, 0.0f);
    }

    public static MutableComponent createBlueYellowGradientText(String text) {
        return ColorUtils.createBlueYellowGradientText(text, 0.0f);
    }

    public static MutableComponent createBlueYellowGradientText(String text, float extraOffset) {
        Color blue = new Color(0, 0, 255);
        Color yellow = new Color(255, 255, 0);
        return ColorUtils.createDualColorGradient(text, blue, yellow, 0.8f, 0.05f, extraOffset);
    }

    public static MutableComponent createOrangeBlueGradientText(String text, float extraOffset) {
        Color warmOrange = new Color(255, 140, 50);
        Color softBlue = new Color(80, 160, 255);
        return ColorUtils.createDualColorGradient(text, warmOrange, softBlue, 0.8f, 0.04f, extraOffset);
    }

    public static MutableComponent createOrangeBlueGradientText(String text, float speed, float spread) {
        Color warmOrange = new Color(255, 140, 50);
        Color softBlue = new Color(80, 160, 255);
        return ColorUtils.createDualColorGradient(text, warmOrange, softBlue, speed, spread);
    }

    public static MutableComponent createOrangeBlueGradientText(String text) {
        return ColorUtils.createOrangeBlueGradientText(text, 0.0f);
    }

    public static MutableComponent createRedOrangeGradientText(String text) {
        Color red = new Color(255, 0, 0);
        Color orange = new Color(255, 165, 0);
        return ColorUtils.createDualColorGradient(text, red, orange, 0.8f, 0.05f, 0.0f);
    }

    public static int getDynamicWhiteBlueColor(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float)((Math.cos(timeOffset + (double)offset) + 1.0) / 2.0);
        Color color1 = new Color(255, 255, 255);
        Color color2 = new Color(100, 200, 255);
        int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
        int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
        int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static int getDynamicOrangeBlueColor(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float)((Math.cos(timeOffset + (double)offset) + 1.0) / 2.0);
        Color color1 = new Color(255, 140, 50);
        Color color2 = new Color(80, 160, 255);
        int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
        int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
        int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static int getDynamicBlueGreenColor(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float)((Math.cos(timeOffset + (double)offset) + 1.0) / 2.0);
        Color color1 = new Color(80, 160, 255);
        Color color2 = new Color(0, 255, 180);
        int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
        int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
        int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static MutableComponent createWhiteBlueGradientText(String text, float extraOffset) {
        Color white = new Color(255, 255, 255);
        Color lightBlue = new Color(100, 200, 255);
        return ColorUtils.createDualColorGradient(text, white, lightBlue, 0.8f, 0.04f, extraOffset);
    }

    public static MutableComponent createWhiteBlueGradientText(String text) {
        return ColorUtils.createWhiteBlueGradientText(text, 0.0f);
    }

    public static MutableComponent createGreenRedGradientText(String text, float extraOffset) {
        Color green = new Color(0, 255, 0);
        Color red = new Color(255, 0, 0);
        return ColorUtils.createDualColorGradient(text, green, red, 0.8f, 0.05f, extraOffset);
    }

    public static MutableComponent createGreenRedGradientText(String text) {
        return ColorUtils.createGreenRedGradientText(text, 0.0f);
    }

    public static MutableComponent createGlitchWhiteBlueGradientText(int length, float extraOffset) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; ++i) {
            sb.append(GLITCH_CHARS[rand.nextInt(GLITCH_CHARS.length)]);
        }
        return ColorUtils.createWhiteBlueGradientText(sb.toString(), extraOffset);
    }

    public static MutableComponent createGlitchGradientText(int length, float extraOffset) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; ++i) {
            sb.append(GLITCH_CHARS[rand.nextInt(GLITCH_CHARS.length)]);
        }
        return ColorUtils.createOrangeBlueGradientText(sb.toString(), extraOffset);
    }

    public static MutableComponent createGlitchGradientText(int length) {
        return ColorUtils.createGlitchGradientText(length, 0.0f);
    }

    public static MutableComponent createGlitchText(String text, int glitchLength) {
        char glitchChar;
        int charIndex;
        int i;
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 5.0;
        for (i = 0; i < glitchLength; ++i) {
            charIndex = (int)((timeOffset + (double)(i * 13)) % (double)GLITCH_CHARS.length);
            glitchChar = GLITCH_CHARS[Math.abs(charIndex) % GLITCH_CHARS.length];
            root.append((Component)Component.literal((String)String.valueOf(glitchChar)));
        }
        root.append((Component)Component.literal((String)text));
        for (i = 0; i < glitchLength; ++i) {
            charIndex = (int)((timeOffset + (double)(i * 17) + (double)glitchLength) % (double)GLITCH_CHARS.length);
            glitchChar = GLITCH_CHARS[Math.abs(charIndex) % GLITCH_CHARS.length];
            root.append((Component)Component.literal((String)String.valueOf(glitchChar)));
        }
        return root;
    }

    public static MutableComponent createGlitchOrangeBlueGradientText(String text, int glitchLength) {
        int charIndex;
        int i;
        StringBuilder fullText = new StringBuilder();
        double timeOffset = (double)System.currentTimeMillis() / 5.0;
        for (i = 0; i < glitchLength; ++i) {
            charIndex = (int)((timeOffset + (double)(i * 13)) % (double)GLITCH_CHARS.length);
            fullText.append(GLITCH_CHARS[Math.abs(charIndex) % GLITCH_CHARS.length]);
        }
        fullText.append(text);
        for (i = 0; i < glitchLength; ++i) {
            charIndex = (int)((timeOffset + (double)(i * 17) + (double)glitchLength) % (double)GLITCH_CHARS.length);
            fullText.append(GLITCH_CHARS[Math.abs(charIndex) % GLITCH_CHARS.length]);
        }
        return ColorUtils.createOrangeBlueGradientText(fullText.toString());
    }

    public static MutableComponent createPurplePinkBlueGreenYellowGradientText(String text, float speed, float spread, float extraOffset) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        Color purple = new Color(128, 0, 255);
        Color pink = new Color(255, 105, 180);
        Color blue = new Color(0, 0, 255);
        Color green = new Color(0, 255, 0);
        Color yellow = new Color(255, 255, 0);
        for (int i = 0; i < text.length(); ++i) {
            float factor;
            Color color2;
            Color color1;
            float position = (float)((timeOffset + (double)((float)i * spread) + (double)extraOffset) % 5.0);
            if ((double)position < 1.0) {
                color1 = purple;
                color2 = pink;
                factor = position;
            } else if ((double)position < 2.0) {
                color1 = pink;
                color2 = blue;
                factor = position - 1.0f;
            } else if ((double)position < 3.0) {
                color1 = blue;
                color2 = green;
                factor = position - 2.0f;
            } else if ((double)position < 4.0) {
                color1 = green;
                color2 = yellow;
                factor = position - 3.0f;
            } else {
                color1 = yellow;
                color2 = purple;
                factor = position - 4.0f;
            }
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createPurplePinkBlueGreenYellowGradientText(String text) {
        return ColorUtils.createPurplePinkBlueGreenYellowGradientText(text, 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createPurplePinkBlueGreenYellowGradientText(String text, float extraOffset) {
        return ColorUtils.createPurplePinkBlueGreenYellowGradientText(text, 0.8f, 0.04f, extraOffset);
    }

    public static MutableComponent createBluePurpleGreenGradientText(String text, float speed, float spread, float extraOffset) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        Color blue = new Color(0, 0, 255);
        Color purple = new Color(128, 0, 255);
        Color green = new Color(0, 255, 0);
        for (int i = 0; i < text.length(); ++i) {
            float factor;
            Color color2;
            Color color1;
            float position = (float)((timeOffset + (double)((float)i * spread) + (double)extraOffset) % 3.0);
            if ((double)position < 1.0) {
                color1 = blue;
                color2 = purple;
                factor = position;
            } else if ((double)position < 2.0) {
                color1 = purple;
                color2 = green;
                factor = position - 1.0f;
            } else {
                color1 = green;
                color2 = blue;
                factor = position - 2.0f;
            }
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createBluePurpleGreenGradientText(String text, float speed) {
        return ColorUtils.createBluePurpleGreenGradientText(text, speed, 0.04f, 0.0f);
    }

    public static MutableComponent createBluePurpleGreenGradientText(String text) {
        return ColorUtils.createBluePurpleGreenGradientText(text, 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createBluePurpleGreenGradientTextWithOffset(String text, float extraOffset) {
        return ColorUtils.createBluePurpleGreenGradientText(text, 0.8f, 0.04f, extraOffset);
    }

    public static MutableComponent createBlueGreenGradientText(String text, float speed, float spread, float extraOffset) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        Color blue = new Color(0, 0, 255);
        Color green = new Color(0, 255, 0);
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double)((float)i * spread) + (double)extraOffset;
            float factor = (float)((Math.cos(angle) + 1.0) / 2.0);
            int r = (int)((float)blue.getRed() + factor * (float)(green.getRed() - blue.getRed()));
            int g = (int)((float)blue.getGreen() + factor * (float)(green.getGreen() - blue.getGreen()));
            int b = (int)((float)blue.getBlue() + factor * (float)(green.getBlue() - blue.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createBlueGreenGradientText(String text, float speed) {
        return ColorUtils.createBlueGreenGradientText(text, speed, 0.04f, 0.0f);
    }

    public static MutableComponent createBlueGreenGradientText(String text) {
        return ColorUtils.createBlueGreenGradientText(text, 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createBlueGreenGradientTextWithOffset(String text, float extraOffset) {
        return ColorUtils.createBlueGreenGradientText(text, 0.8f, 0.04f, extraOffset);
    }

    public static MutableComponent createBlueRedGradientText(String text) {
        Color blue = new Color(0, 0, 255);
        Color red = new Color(255, 0, 0);
        return ColorUtils.createDualColorGradient(text, blue, red, 0.8f, 0.05f, 0.0f);
    }

    public static MutableComponent createYellowandRedGradientText(String text, float speed) {
        Color yellow = new Color(255, 255, 0);
        Color red = new Color(255, 0, 0);
        return ColorUtils.createDualColorGradient(text, yellow, red, speed, 0.05f);
    }

    public static MutableComponent createYellowCyanGradientText(String text, float speed) {
        Color yellow = new Color(255, 255, 0);
        Color cyan = new Color(0, 255, 255);
        return ColorUtils.createDualColorGradient(text, yellow, cyan, speed, 0.05f);
    }

    public static String toRoman(int n) {
        if (n <= 0) {
            return String.valueOf(n);
        }
        String[] roman = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; ++i) {
            while (n >= values[i]) {
                sb.append(roman[i]);
                n -= values[i];
            }
        }
        return sb.toString();
    }

    public static MutableComponent createBlueYellowPurpleGradientText(String text) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        Color blue = new Color(0, 100, 255);
        Color yellow = new Color(255, 255, 0);
        Color purple = new Color(180, 0, 255);
        for (int i = 0; i < text.length(); ++i) {
            float factor;
            Color color2;
            Color color1;
            float position = (float)((timeOffset + (double)((float)i * 0.04f)) % 3.0);
            if ((double)position < 1.0) {
                color1 = blue;
                color2 = yellow;
                factor = position;
            } else if ((double)position < 2.0) {
                color1 = yellow;
                color2 = purple;
                factor = position - 1.0f;
            } else {
                color1 = purple;
                color2 = blue;
                factor = position - 2.0f;
            }
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createYellowGreenPurpleGradientText(String text) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        Color yellow = new Color(255, 255, 0);
        Color green = new Color(0, 255, 100);
        Color purple = new Color(180, 0, 255);
        for (int i = 0; i < text.length(); ++i) {
            float factor;
            Color color2;
            Color color1;
            float position = (float)((timeOffset + (double)((float)i * 0.04f)) % 3.0);
            if ((double)position < 1.0) {
                color1 = yellow;
                color2 = green;
                factor = position;
            } else if ((double)position < 2.0) {
                color1 = green;
                color2 = purple;
                factor = position - 1.0f;
            } else {
                color1 = purple;
                color2 = yellow;
                factor = position - 2.0f;
            }
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    private static float smoothEaseInOut(float t) {
        return t * t * t * (t * (6.0f * t - 15.0f) + 10.0f);
    }

    public static int getAnimatedColor(long time, int color1, int color2, int color3) {
        float factor;
        int toColor;
        int fromColor;
        float cycle = (float)((double)((float)time * 0.05f) % (Math.PI * 2));
        float normalizedTime = (float)((double)cycle / (Math.PI * 2) * 3.0);
        if (normalizedTime < 1.0f) {
            fromColor = color1;
            toColor = color2;
            factor = normalizedTime;
        } else if (normalizedTime < 2.0f) {
            fromColor = color2;
            toColor = color3;
            factor = normalizedTime - 1.0f;
        } else {
            fromColor = color3;
            toColor = color1;
            factor = normalizedTime - 2.0f;
        }
        factor = ColorUtils.smoothEaseInOut(factor);
        int a1 = fromColor >> 24 & 0xFF;
        int r1 = fromColor >> 16 & 0xFF;
        int g1 = fromColor >> 8 & 0xFF;
        int b1 = fromColor & 0xFF;
        int a2 = toColor >> 24 & 0xFF;
        int r2 = toColor >> 16 & 0xFF;
        int g2 = toColor >> 8 & 0xFF;
        int b2 = toColor & 0xFF;
        int a = (int)((float)a1 + (float)(a2 - a1) * factor);
        int r = (int)((float)r1 + (float)(r2 - r1) * factor);
        int g = (int)((float)g1 + (float)(g2 - g1) * factor);
        int b = (int)((float)b1 + (float)(b2 - b1) * factor);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int getAnimatedRainbowColor(long time, float speed, int alpha) {
        float cycle = (float)((double)((float)time * speed) % (Math.PI * 2));
        float hue = (float)((double)cycle / (Math.PI * 2));
        int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        return (alpha & 0xFF) << 24 | rgb & 0xFFFFFF;
    }

    public static int getDynamicRainbowColor(float offset, float speed) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        float hue = (float)((timeOffset + (double)offset) % 1.0);
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }

    public static MutableComponent createWobblingText(String text, float speed) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        for (int i = 0; i < text.length(); ++i) {
            double wobble = Math.sin(timeOffset * 3.0 + (double)(i * 7)) * 0.3;
            float hue = (float)((timeOffset + (double)(i * 11) + wobble) % 1.0);
            int color = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createTextWithWobblingHighlight(String fullText, String highlight, float wobbleSpeed) {
        MutableComponent root = Component.empty();
        int index = fullText.indexOf(highlight);
        if (index == -1) {
            return ColorUtils.createRainbowText(fullText, 0.8f, 0.04f);
        }
        String before = fullText.substring(0, index);
        String after = fullText.substring(index + highlight.length());
        if (!before.isEmpty()) {
            root.append((Component)ColorUtils.createRainbowText(before, 0.8f, 0.04f));
        }
        root.append((Component)ColorUtils.createWobblingText(highlight, wobbleSpeed));
        if (!after.isEmpty()) {
            root.append((Component)ColorUtils.createRainbowText(after, 0.8f, 0.04f));
        }
        return root;
    }

    public static MutableComponent createShakingText(String text, float speed) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * (double)speed;
        for (int i = 0; i < text.length(); ++i) {
            float hue = (float)((timeOffset + (double)((float)i * 0.15f)) % 1.0);
            int color = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color)).withObfuscated(Boolean.valueOf(true))));
        }
        return root;
    }

    public static MutableComponent createTextWithShakingHighlight(String fullText, String highlight, float shakeSpeed) {
        MutableComponent root = Component.empty();
        int index = fullText.indexOf(highlight);
        if (index == -1) {
            return ColorUtils.createRainbowText(fullText, 0.8f, 0.04f);
        }
        String before = fullText.substring(0, index);
        String after = fullText.substring(index + highlight.length());
        if (!before.isEmpty()) {
            root.append((Component)ColorUtils.createRainbowText(before, 0.8f, 0.04f));
        }
        root.append((Component)ColorUtils.createShakingText(highlight, shakeSpeed));
        if (!after.isEmpty()) {
            root.append((Component)ColorUtils.createRainbowText(after, 0.8f, 0.04f));
        }
        return root;
    }

    public static MutableComponent createOrangeCyanGradientText(String text) {
        Color orange = new Color(255, 165, 0);
        Color cyan = new Color(0, 255, 255);
        return ColorUtils.createDualColorGradient(text, orange, cyan, 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createGreenPurpleGradientText(String text) {
        Color green = new Color(0, 255, 100);
        Color purple = new Color(180, 0, 255);
        return ColorUtils.createDualColorGradient(text, green, purple, 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createGoldSilverGradientText(String text) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.2;
        Color gold = new Color(255, 215, 0);
        Color silver = new Color(192, 192, 192);
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double)((float)i * 0.12f);
            float factor = (float)((Math.sin(angle) + 1.0) / 2.0);
            int r = (int)((float)gold.getRed() + factor * (float)(silver.getRed() - gold.getRed()));
            int g = (int)((float)gold.getGreen() + factor * (float)(silver.getGreen() - gold.getGreen()));
            int b = (int)((float)gold.getBlue() + factor * (float)(silver.getBlue() - gold.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createBlackSilverGradientText(String text) {
        return ColorUtils.createDualColorGradient(text, new Color(30, 30, 30), new Color(200, 200, 200), 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createGoldShinyGradientText(String text) {
        MutableComponent root = Component.empty();
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.8;
        Color goldBright = new Color(255, 230, 100);
        Color goldDark = new Color(180, 130, 0);
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double)((float)i * 0.06f);
            float factor = (float)((Math.sin(angle) + 1.0) / 2.0);
            float shiny = factor * factor;
            int r = (int)((float)goldDark.getRed() + shiny * (float)(goldBright.getRed() - goldDark.getRed()));
            int g = (int)((float)goldDark.getGreen() + shiny * (float)(goldBright.getGreen() - goldDark.getGreen()));
            int b = (int)((float)goldDark.getBlue() + shiny * (float)(goldBright.getBlue() - goldDark.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int)color))));
        }
        return root;
    }

    public static MutableComponent createSeaBlueSkyBlueGradientText(String text) {
        return ColorUtils.createDualColorGradient(text, new Color(0, 100, 180), new Color(100, 200, 255), 0.8f, 0.04f, 0.0f);
    }

    public static int getDynamicBluePinkColor(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float)((Math.cos(timeOffset + (double)offset) + 1.0) / 2.0);
        Color blue = new Color(50, 100, 255);
        Color pink = new Color(255, 100, 200);
        int r = (int)((float)blue.getRed() + factor * (float)(pink.getRed() - blue.getRed()));
        int g = (int)((float)blue.getGreen() + factor * (float)(pink.getGreen() - blue.getGreen()));
        int b = (int)((float)blue.getBlue() + factor * (float)(pink.getBlue() - blue.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static int getDynamicOrangeBlueSineColor(float x, float y, float screenW, float screenH) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.2;
        float diagonal = (x / screenW + (1.0f - y / screenH)) / 2.0f;
        double angle = timeOffset + (double)diagonal * Math.PI * 2.0;
        float factor = (float)((Math.sin(angle) + 1.0) / 2.0);
        Color orange = new Color(255, 165, 0);
        Color blue = new Color(0, 100, 255);
        int r = (int)((float)orange.getRed() + factor * (float)(blue.getRed() - orange.getRed()));
        int g = (int)((float)orange.getGreen() + factor * (float)(blue.getGreen() - orange.getGreen()));
        int b = (int)((float)orange.getBlue() + factor * (float)(blue.getBlue() - orange.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static int getDynamicBlueWhiteOrangeSineColor(float x, float y, float screenW, float screenH) {
        int b;
        int g;
        int r;
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.2;
        float diagonal = (x / screenW + (1.0f - y / screenH)) / 2.0f;
        double angle = timeOffset + (double)diagonal * Math.PI * 2.0;
        float factor = (float)((Math.sin(angle) + 1.0) / 2.0);
        Color blue = new Color(0, 100, 255);
        Color white = new Color(255, 255, 255);
        Color orange = new Color(255, 165, 0);
        if (factor < 0.5f) {
            float t = factor * 2.0f;
            r = (int)((float)blue.getRed() + t * (float)(white.getRed() - blue.getRed()));
            g = (int)((float)blue.getGreen() + t * (float)(white.getGreen() - blue.getGreen()));
            b = (int)((float)blue.getBlue() + t * (float)(white.getBlue() - blue.getBlue()));
        } else {
            float t = (factor - 0.5f) * 2.0f;
            r = (int)((float)white.getRed() + t * (float)(orange.getRed() - white.getRed()));
            g = (int)((float)white.getGreen() + t * (float)(orange.getGreen() - white.getGreen()));
            b = (int)((float)white.getBlue() + t * (float)(orange.getBlue() - white.getBlue()));
        }
        return new Color(r, g, b).getRGB();
    }

    public static int getDynamicRainbowColor(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.2;
        float hue = (float)((timeOffset + (double)offset) % 1.0);
        return Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }

    public static MutableComponent createCyanGreenGradientText(String text) {
        Color cyan = new Color(0, 255, 200);
        Color green = new Color(0, 200, 50);
        return ColorUtils.createDualColorGradient(text, cyan, green, 0.8f, 0.04f, 0.0f);
    }

    public static MutableComponent createRedPurpleGradientText(String text) {
        Color red = new Color(255, 0, 0);
        Color purple = new Color(180, 0, 255);
        return ColorUtils.createDualColorGradient(text, red, purple, 0.8f, 0.04f, 0.0f);
    }

    public static int getDynamicCyanGreenColor(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float)((Math.cos(timeOffset + (double)offset) + 1.0) / 2.0);
        Color cyan = new Color(0, 220, 200);
        Color green = new Color(0, 180, 50);
        int r = (int)((float)cyan.getRed() + factor * (float)(green.getRed() - cyan.getRed()));
        int g = (int)((float)cyan.getGreen() + factor * (float)(green.getGreen() - cyan.getGreen()));
        int b = (int)((float)cyan.getBlue() + factor * (float)(green.getBlue() - cyan.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static int getDynamicRedPurpleColor2(float offset) {
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 1.6;
        float factor = (float)((Math.cos(timeOffset + (double)offset) + 1.0) / 2.0);
        Color red = new Color(200, 0, 0);
        Color purple = new Color(120, 0, 200);
        int r = (int)((float)red.getRed() + factor * (float)(purple.getRed() - red.getRed()));
        int g = (int)((float)red.getGreen() + factor * (float)(purple.getGreen() - red.getGreen()));
        int b = (int)((float)red.getBlue() + factor * (float)(purple.getBlue() - red.getBlue()));
        return new Color(r, g, b).getRGB();
    }

    public static MutableComponent createPinkOrangeGradientText(String text) {
        Color pink = new Color(15884945);
        Color orange = new Color(16231190);
        return ColorUtils.createDualColorGradient(text, pink, orange, 0.8f, 0.05f, 0.0f);
    }

    public static MutableComponent createRedOrangeBlueGradientText(String text) {
        MutableComponent result = Component.literal((String)"");
        double timeOffset = (double)System.currentTimeMillis() / 1000.0 * 0.8;
        Color red = new Color(255, 30, 30);
        Color orange = new Color(255, 165, 0);
        Color blue = new Color(50, 130, 255);
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float factor;
            Color color2;
            Color color1;
            float ratio = (float)(((double)i / (double)Math.max(len - 1, 1) + timeOffset) % 1.0);
            float position = ratio * 2.0f;
            if (position < 1.0f) {
                color1 = red;
                color2 = orange;
                factor = position;
            } else {
                color1 = orange;
                color2 = blue;
                factor = position - 1.0f;
            }
            int r = (int)((float)color1.getRed() + factor * (float)(color2.getRed() - color1.getRed()));
            int g = (int)((float)color1.getGreen() + factor * (float)(color2.getGreen() - color1.getGreen()));
            int b = (int)((float)color1.getBlue() + factor * (float)(color2.getBlue() - color1.getBlue()));
            int color = r << 16 | g << 8 | b;
            result.append((Component)Component.literal((String)String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(color)));
        }
        return result;
    }
}

