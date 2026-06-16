/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.mixin.LivingEntityAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { LivingEntity.class })
public class ShuangshengSwordLivingEntityMixin {
    @Inject(method = { "setHealth" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void onSetHealth(float health, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.getPersistentData().getBoolean("manbov2c_shuangsheng_forcedeath")) {
            self.getPersistentData().remove("manbov2c_shuangsheng_forcedeath");
            ((LivingEntityAccessor) (Object) self).manbo_v2c$setHealthDirect(0.0f);
            ci.cancel();
        }
    }
}
