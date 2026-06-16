/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package com.manbo.v2c.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { LivingEntity.class })
public class LivingEntityAccessor {
    @Shadow
    private float health;

    public void manbo_v2c$setHealthDirect(float health) {
        this.health = health;
    }

    public float manbo_v2c$getHealthDirect() {
        return this.health;
    }
}
