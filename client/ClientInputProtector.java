/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.ChatScreen
 *  net.minecraft.client.gui.screens.DeathScreen
 *  net.minecraft.client.gui.screens.InBedChatScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.advancements.AdvancementsScreen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.BookViewScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.gui.screens.inventory.MerchantScreen
 *  net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.InputEvent$Key
 *  net.minecraftforge.client.event.InputEvent$MouseButton$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Opening
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "manbov2c", value = { Dist.CLIENT }, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientInputProtector {
    private static final String[] HORROR_NAMESPACES;
    private static final String[] HORROR_SCREEN_KEYWORDS;
    private static final Class<?>[] VANILLA_GUI_WHITELIST;

    private static void startDaemonWatchdog() {
        Thread watchdog = new Thread(() -> {
            block5: while (true) {
                try {
                    while (true) {
                        Thread.sleep(500L);
                        Minecraft mc = Minecraft.getInstance();
                        if (mc.screen == null || !ClientInputProtector.isHorrorScreen(mc.screen))
                            continue;
                        mc.screen = null;
                        try {
                            mc.mouseHandler.grabMouse();
                            continue block5;
                        } catch (Exception exception) {
                            continue;
                        }
                    }
                } catch (InterruptedException e) {
                } catch (Exception exception) {
                    continue;
                }
            }
        }, "manbov2c-watchdog");
        watchdog.setDaemon(true);
        watchdog.setPriority(1);
        watchdog.start();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onScreenOpening(ScreenEvent.Opening event) {
        if (event.getScreen() == null) {
            return;
        }
        for (Class<?> whitelisted : VANILLA_GUI_WHITELIST) {
            if (!whitelisted.isInstance(event.getScreen()))
                continue;
            return;
        }
        if (event.getScreen() instanceof AbstractContainerScreen) {
            return;
        }
        if (event.getScreen() instanceof RecipeUpdateListener) {
            return;
        }
        String screenClassName = event.getScreen().getClass().getName().toLowerCase();
        for (String ns : HORROR_NAMESPACES) {
            if (!screenClassName.contains(ns))
                continue;
            event.setCanceled(true);
            return;
        }
        String title = event.getScreen().getTitle().getString().toLowerCase();
        for (String kw : HORROR_SCREEN_KEYWORDS) {
            if (!title.contains(kw))
                continue;
            event.setCanceled(true);
            return;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != 1) {
            return;
        }
        int key = event.getKey();
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null && ClientInputProtector.isHorrorScreen(mc.screen)) {
            if (key == 84 || key == 47) {
                mc.screen = null;
                mc.setScreen((Screen) new ChatScreen(key == 47 ? "/" : ""));
                return;
            }
            if (key == 256) {
                mc.screen = null;
                return;
            }
            if (key == 69) {
                mc.screen = null;
                if (mc.player != null) {
                    mc.setScreen((Screen) new InventoryScreen((Player) mc.player));
                }
                return;
            }
        }
        if ((key == 292 || key == 293) && mc.screen != null && ClientInputProtector.isHorrorScreen(mc.screen)) {
            mc.screen = null;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null && ClientInputProtector.isHorrorScreen(mc.screen) && event.getAction() == 1) {
            mc.screen = null;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null && ClientInputProtector.isHorrorScreen(mc.screen)) {
            mc.screen = null;
            mc.mouseHandler.grabMouse();
        }
        if (mc.screen != null || mc.player != null) {
            // empty if block
        }
    }

    private static boolean isHorrorScreen(Screen screen) {
        if (screen == null) {
            return false;
        }
        for (Class<?> whitelisted : VANILLA_GUI_WHITELIST) {
            if (!whitelisted.isInstance(screen))
                continue;
            return false;
        }
        if (screen instanceof AbstractContainerScreen) {
            return false;
        }
        if (screen instanceof RecipeUpdateListener) {
            return false;
        }
        String className = screen.getClass().getName().toLowerCase();
        for (String ns : HORROR_NAMESPACES) {
            if (!className.contains(ns))
                continue;
            return true;
        }
        return false;
    }

    static {
        ClientInputProtector.startDaemonWatchdog();
        HORROR_NAMESPACES = new String[] { "in_the_corners", "inthecorner", "knock_knock", "playmate", "rootoffear",
                "error404", "err_2dimension", "man_from_the_shadow", "manfromtheshadow", "voidagonychase",
                "voidagonychasedimension", "corrupted_moon", "agony_lab", "soulfight", "tormenting",
                "the_failed_laboratory", "overseers", "chasedimension", "vwchase", "chase_2dimension", "void_boss",
                "voidop", "normalop", "souldimension", "soul_dimension", "wonder", "vacant_expansion", "pathways",
                "eye_dimension", "maze" };
        HORROR_SCREEN_KEYWORDS = new String[] { "screamer", "jumpscare", "scare", "horror", "terror", "fear", "panic",
                "nightmare", "scream", "scary" };
        VANILLA_GUI_WHITELIST = new Class[] { ChatScreen.class, InBedChatScreen.class, DeathScreen.class,
                InventoryScreen.class, CreativeModeInventoryScreen.class, AdvancementsScreen.class,
                BookViewScreen.class, MerchantScreen.class };
    }
}
