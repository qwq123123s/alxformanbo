/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.DeathScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.ScreenEvent$Opening
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class TianyuanClientHandler {
    private static final String[] HORROR_NAMESPACES = new String[]{"knock_knock", "rootoffear", "inthecorner", "in_the_corners", "error404"};

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        String[] horrorKeywords;
        Screen screen = event.getScreen();
        if (screen == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        if (!TianyuanClientHandler.hasFullTianyuanArmor(player)) {
            return;
        }
        if (screen instanceof DeathScreen) {
            event.setCanceled(true);
            return;
        }
        String screenClass = screen.getClass().getName().toLowerCase();
        for (String ns : HORROR_NAMESPACES) {
            if (!screenClass.contains(ns)) continue;
            event.setCanceled(true);
            return;
        }
        String title = screen.getTitle().getString().toLowerCase();
        for (String kw : horrorKeywords = new String[]{"screamer", "jumpscare", "scare", "horror", "terror", "fear"}) {
            if (!title.contains(kw)) continue;
            event.setCanceled(true);
            return;
        }
    }

    private static boolean hasFullTianyuanArmor(LocalPlayer player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        return head.is((Item)ModItems.TIANYUAN_HELMET.get()) && chest.is((Item)ModItems.TIANYUAN_CHESTPLATE.get()) && legs.is((Item)ModItems.TIANYUAN_LEGGINGS.get()) && feet.is((Item)ModItems.TIANYUAN_BOOTS.get());
    }
}

