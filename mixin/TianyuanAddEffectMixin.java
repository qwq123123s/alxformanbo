/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.item.equipments.TianyuanArmorHandler;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { LivingEntity.class })
public class TianyuanAddEffectMixin {
    private static final Set<MobEffect> BAD_EFFECTS = new HashSet<MobEffect>();

    @Inject(method = { "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z" }, at = {
            @At(value = "HEAD") }, cancellable = true)
    private void manbov2c$tianyuan_blockBadEffect(MobEffectInstance effectInstance,
            CallbackInfoReturnable<Boolean> cir) {
        if (effectInstance == null) {
            return;
        }
        Entity self = (Entity) (Object) this;
        if (!(self instanceof Player)) {
            return;
        }
        Player player = (Player) self;
        if (player.level().isClientSide()) {
            return;
        }
        if (!TianyuanArmorHandler.hasFullTianyuanArmor(player)) {
            return;
        }
        MobEffect effect = effectInstance.getEffect();
        if (BAD_EFFECTS.contains(effect)) {
            cir.setReturnValue(false);
        }
    }

    static {
        BAD_EFFECTS.add(MobEffects.POISON);
        BAD_EFFECTS.add(MobEffects.WITHER);
        BAD_EFFECTS.add(MobEffects.BLINDNESS);
        BAD_EFFECTS.add(MobEffects.DARKNESS);
        BAD_EFFECTS.add(MobEffects.HUNGER);
        BAD_EFFECTS.add(MobEffects.WEAKNESS);
        BAD_EFFECTS.add(MobEffects.MOVEMENT_SLOWDOWN);
        BAD_EFFECTS.add(MobEffects.DIG_SLOWDOWN);
        BAD_EFFECTS.add(MobEffects.CONFUSION);
        BAD_EFFECTS.add(MobEffects.UNLUCK);
        BAD_EFFECTS.add(MobEffects.LEVITATION);
        BAD_EFFECTS.add(MobEffects.GLOWING);
    }
}
