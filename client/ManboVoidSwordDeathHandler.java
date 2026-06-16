/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.TickEvent$PlayerTickEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.client;

import com.manbo.v2c.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c", bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ManboVoidSwordDeathHandler {
    private static final String TAG_LAST_USED = "manbovoid_death_last";
    private static final long COOLDOWN_TICKS = 360L;

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        long lastUsed;
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        boolean hasSword = false;
        for (ItemStack stack : player.getHandSlots()) {
            if (!stack.is((Item)ModItems.MANBO_VOID_SWORD.get())) continue;
            hasSword = true;
            break;
        }
        if (!hasSword) {
            return;
        }
        CompoundTag data = player.getPersistentData();
        long gameTime = player.level().getGameTime();
        if (gameTime < (lastUsed = data.getLong(TAG_LAST_USED)) + 360L) {
            return;
        }
        data.putLong(TAG_LAST_USED, gameTime);
        data.putBoolean("manbovoid_revive", true);
        player.displayClientMessage((Component)Component.literal((String)"\u00a75\u00a7l\u865a\u7a7a\u5251\u7684\u5951\u7ea6\u4fdd\u62a4\u4e86\u4f60\uff0c\u4f60\u4ece\u865a\u7a7a\u4e2d\u5f52\u6765"), true);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.player.level().isClientSide()) {
            return;
        }
        Player player = event.player;
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player2 = (ServerPlayer)player;
        CompoundTag data = player2.getPersistentData();
        if (!data.getBoolean("manbovoid_revive")) {
            return;
        }
        data.remove("manbovoid_revive");
        if (player2.isDeadOrDying() || !player2.isAlive()) {
            player2.setHealth(player2.getMaxHealth());
            player2.setXRot(player2.xRotO);
            player2.setYRot(player2.yRotO);
            player2.removeAllEffects();
            player2.setRemainingFireTicks(0);
            player2.clearFire();
        }
    }
}

