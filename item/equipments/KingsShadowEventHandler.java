/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.advancements.Advancement
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.TickEvent$PlayerTickEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$ItemCraftedEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$ItemPickupEvent
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.equipments;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.network.KingsShadowToastPacket;
import com.manbo.v2c.network.NetworkHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class KingsShadowEventHandler {
    private static final float LOW_HEALTH_THRESHOLD = 6.0f;
    private static final Set<UUID> awardedPlayers = new HashSet<UUID>();
    private static final Set<UUID> previousTickHasKingsShadow = new HashSet<UUID>();

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        Player player = event.getEntity();
        ItemStack stack = event.getStack();
        if (stack.is((Item)ModItems.KINGS_SHADOW.get())) {
            KingsShadowEventHandler.grantKingsShadowAdvancement(player);
        }
    }

    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        Player player;
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        Player player2 = player = event.getEntity() instanceof Player ? event.getEntity() : null;
        if (player == null) {
            return;
        }
        ItemStack result = event.getCrafting();
        if (result.is((Item)ModItems.KINGS_SHADOW.get())) {
            KingsShadowEventHandler.grantKingsShadowAdvancement(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        float health;
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.player.level().isClientSide()) {
            return;
        }
        Player player = event.player;
        boolean hasIt = false;
        if (player.getItemBySlot(EquipmentSlot.CHEST).is((Item)ModItems.KINGS_SHADOW.get())) {
            hasIt = true;
        }
        if (!hasIt) {
            for (ItemStack stack : player.getInventory().items) {
                if (!stack.is((Item)ModItems.KINGS_SHADOW.get())) continue;
                hasIt = true;
                break;
            }
        }
        if (!hasIt && player.getOffhandItem().is((Item)ModItems.KINGS_SHADOW.get())) {
            hasIt = true;
        }
        if (hasIt) {
            KingsShadowEventHandler.grantKingsShadowAdvancement(player);
        }
        boolean currentlyHas = player.getItemBySlot(EquipmentSlot.CHEST).is((Item)ModItems.KINGS_SHADOW.get());
        UUID pid = player.getUUID();
        boolean previouslyHad = previousTickHasKingsShadow.contains(pid);
        if (currentlyHas && !previouslyHad && (health = player.getHealth()) <= 6.0f && player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            NetworkHandler.sendToPlayer(new KingsShadowToastPacket(1), serverPlayer);
        }
        if (currentlyHas) {
            previousTickHasKingsShadow.add(pid);
        } else {
            previousTickHasKingsShadow.remove(pid);
        }
    }

    private static void grantKingsShadowAdvancement(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        if (awardedPlayers.contains(serverPlayer.getUUID())) {
            return;
        }
        ResourceLocation advId = new ResourceLocation("manbov2c", "kings_shadow");
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(advId);
        if (advancement != null) {
            serverPlayer.getAdvancements().award(advancement, "has_kings_shadow");
            awardedPlayers.add(serverPlayer.getUUID());
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        if (!player.getItemBySlot(EquipmentSlot.CHEST).is((Item)ModItems.KINGS_SHADOW.get())) {
            return;
        }
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            NetworkHandler.sendToPlayer(new KingsShadowToastPacket(0), serverPlayer);
        }
    }
}

