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

import com.manbo.v2c.client.ManboFinalSwordOverlayRenderer;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ManboFinalSwordHitPacket {
    public static void encode(ManboFinalSwordHitPacket packet, FriendlyByteBuf buf) {
    }

    public static ManboFinalSwordHitPacket decode(FriendlyByteBuf buf) {
        return new ManboFinalSwordHitPacket();
    }

    public static void handle(ManboFinalSwordHitPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> ManboFinalSwordOverlayRenderer.onHit()));
        ctx.get().setPacketHandled(true);
    }
}

