package com.manbo.v2c.mixin;

import com.manbo.v2c.server.DefenseController;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 防踢出 Mixin — 玩家免疫其他模组的踢出
 * <p>
 * 拦截服务端下发的 ClientboundDisconnectPacket 踢出数据包，
 * 阻止玩家被强制断开连接。
 * 属于诗宝之爱防御体系的网络层防护。
 */
@Mixin(ClientPacketListener.class)
public abstract class AntiKickMixin {

    /**
     * 在 handleDisconnect 执行前拦截，如果防踢出保护已激活则取消处理
     */
    @Inject(method = "handleDisconnect", at = @At("HEAD"), cancellable = true)
    private void manbov2c_onHandleDisconnect(ClientboundDisconnectPacket packet, CallbackInfo ci) {
        if (DefenseController.isAntiKickActive()) {
            ci.cancel();
        }
    }
}