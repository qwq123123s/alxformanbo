/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraftforge.network.NetworkDirection
 *  net.minecraftforge.network.NetworkRegistry$ChannelBuilder
 *  net.minecraftforge.network.simple.SimpleChannel
 */
package com.manbo.v2c.network;

import com.manbo.v2c.network.KingsShadowToastPacket;
import com.manbo.v2c.network.ManboFinalSwordHitPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named((ResourceLocation)new ResourceLocation("manbov2c", "main")).clientAcceptedVersions("1"::equals).serverAcceptedVersions("1"::equals).networkProtocolVersion(() -> "1").simpleChannel();
    private static int packetId = 0;

    public static void init() {
        CHANNEL.registerMessage(packetId++, KingsShadowToastPacket.class, KingsShadowToastPacket::encode, KingsShadowToastPacket::decode, KingsShadowToastPacket::handle);
        CHANNEL.registerMessage(packetId++, ManboFinalSwordHitPacket.class, ManboFinalSwordHitPacket::encode, ManboFinalSwordHitPacket::decode, ManboFinalSwordHitPacket::handle);
    }

    public static void sendToPlayer(Object packet, ServerPlayer player) {
        CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}

