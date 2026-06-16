/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.mixin.LivingEntityAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { LivingEntity.class })
public class SuperSwordLivingEntityMixin {
    @Unique
    private boolean manbov2c$superKilled = false;

    @Inject(method = { "setHealth" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void manbov2c$super_onSetHealth(float health, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getPersistentData().getBoolean("manbov2c_super_forcedeath")) {
            self.getPersistentData().remove("manbov2c_super_forcedeath");
            ((LivingEntityAccessor) (Object) self).manbo_v2c$setHealthDirect(0.0f);
            self.discard();
            ci.cancel();
        }
    }

    @Inject(method = { "tick" }, at = { @At(value = "HEAD") })
    private void manbov2c$super_onTick(CallbackInfo ci) {
        if (this.manbov2c$superKilled) {
            return;
        }
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        if (!self.getPersistentData().getBoolean("manbov2c_super_voidkill")) {
            return;
        }
        this.manbov2c$superKilled = true;
        self.getPersistentData().remove("manbov2c_super_voidkill");
        self.setPos(Double.NaN, Double.NaN, Double.NaN);
        self.getPersistentData().remove("manbov2c_super_forcedeath");
        self.discard();
    }

    @Inject(method = { "hurt" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void manbov2c$super_onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getPersistentData().getBoolean("manbov2c_super_bypasshurt")) {
            self.getPersistentData().remove("manbov2c_super_bypasshurt");
            self.getPersistentData().putBoolean("manbov2c_super_forcedeath", true);
            self.setHealth(0.0f);
            cir.setReturnValue(true);
        }
    }
}
