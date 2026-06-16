/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.GameType
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.server.CommandAndGamemodeProtector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { ServerPlayer.class })
public abstract class FinalSwordGameModeMixin {
    @Inject(method = { "setGameMode" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void manbov2c_protectGameMode(GameType gameType, CallbackInfo ci) {
        GameType currentType;
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (CommandAndGamemodeProtector.isPlayerProtected(player)
                && gameType != (currentType = player.gameMode.getGameModeForPlayer()) && gameType != GameType.CREATIVE
                && gameType != GameType.SPECTATOR) {
            ci.cancel();
        }
    }
}
