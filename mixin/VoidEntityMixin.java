/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.manbo.v2c.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { Entity.class })
public abstract class VoidEntityMixin {
    @Unique
    private boolean manbov2c$voidKilled = false;

    @Inject(method = { "tick" }, at = { @At(value = "HEAD") })
    private void manbov2c$onTick(CallbackInfo ci) {
        if (this.manbov2c$voidKilled) {
            return;
        }
        Entity self = (Entity) (Object) this;
        if (self.level().isClientSide()) {
            return;
        }
        CompoundTag data = self.getPersistentData();
        if (!data.getBoolean("manbovoid_kill")) {
            return;
        }
        this.manbov2c$voidKilled = true;
        data.remove("manbovoid_kill");
        CompoundTag emptyTag = new CompoundTag();
        emptyTag.putString("id", self.getEncodeId() != null ? self.getEncodeId() : "minecraft:entity");
        self.load(emptyTag);
        self.remove(Entity.RemovalReason.DISCARDED);
    }
}
