/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package com.manbo.v2c.network;

import com.manbo.v2c.client.KingsShadowOverlayRenderer;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class KingsShadowToastPacket {
    private final int type;

    public KingsShadowToastPacket(int type) {
        this.type = type;
    }

    public static void encode(KingsShadowToastPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.type);
    }

    public static KingsShadowToastPacket decode(FriendlyByteBuf buf) {
        return new KingsShadowToastPacket(buf.readInt());
    }

    public static void handle(KingsShadowToastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> KingsShadowOverlayRenderer.onPacketReceived(packet.type)));
        ctx.get().setPacketHandled(true);
    }

    public int getType() {
        return this.type;
    }
}

