/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.util.ManboDeathList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class FinalSwordAntiReviveMixin {
    @Inject(method={"remove"}, at={@At(value="HEAD")}, cancellable=true)
    private void manbov2c_antiRevive_onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (ManboDeathList.isUUIDBlacklisted(entity.getUUID())) {
            ci.cancel();
        }
    }

    @Inject(method={"setRemoved"}, at={@At(value="HEAD")}, cancellable=true)
    private void manbov2c_antiRevive_onSetRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (ManboDeathList.isUUIDBlacklisted(entity.getUUID())) {
            ci.cancel();
        }
    }
}

