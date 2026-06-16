/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { Player.class })
public abstract class FinalSwordPlayerMixin {
    private static boolean hasFinalSword(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.is((Item) ModItems.MANBO_FINAL_SWORD.get()))
                continue;
            return true;
        }
        return player.getMainHandItem().is((Item) ModItems.MANBO_FINAL_SWORD.get())
                || player.getOffhandItem().is((Item) ModItems.MANBO_FINAL_SWORD.get());
    }

    @Inject(method = { "isInvulnerableTo" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void manbov2c_final_onIsInvulnerable(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (FinalSwordPlayerMixin.hasFinalSword(player)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = { "tick" }, at = { @At(value = "HEAD") })
    private void manbov2c_final_onTick(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (FinalSwordPlayerMixin.hasFinalSword(player)) {
            if (player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
            player.clearFire();
            player.removeAllEffects();
        }
    }
}