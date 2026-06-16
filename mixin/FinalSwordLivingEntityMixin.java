/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { LivingEntity.class })
public abstract class FinalSwordLivingEntityMixin {
    private static boolean hasFinalSword(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.is((Item) ModItems.MANBO_FINAL_SWORD.get()))
                continue;
            return true;
        }
        return player.getMainHandItem().is((Item) ModItems.MANBO_FINAL_SWORD.get())
                || player.getOffhandItem().is((Item) ModItems.MANBO_FINAL_SWORD.get());
    }

    @Inject(method = { "hurt" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void manbov2c_final_onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Player player;
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player && FinalSwordLivingEntityMixin.hasFinalSword(player = (Player) entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = { "getMaxHealth" }, at = { @At(value = "RETURN") }, cancellable = true)
    private void manbov2c_final_onGetMaxHealth(CallbackInfoReturnable<Float> cir) {
        Player player;
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player && FinalSwordLivingEntityMixin.hasFinalSword(player = (Player) entity)) {
            cir.setReturnValue(Float.valueOf(Float.MAX_VALUE));
        }
    }

    @Inject(method = { "getHealth" }, at = { @At(value = "RETURN") }, cancellable = true)
    private void manbov2c_final_onGetHealth(CallbackInfoReturnable<Float> cir) {
        Player player;
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player && FinalSwordLivingEntityMixin.hasFinalSword(player = (Player) entity)) {
            cir.setReturnValue(Float.valueOf(Float.MAX_VALUE));
        }
    }
}
