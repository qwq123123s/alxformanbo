/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.EquipmentSlot
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { LivingEntity.class })
public class AlepbLivingEntityMixin {
    @Inject(method = { "hurt" }, at = { @At(value = "HEAD") }, cancellable = true)
    private void manbov2c_onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player)) {
            return;
        }
        Player player = (Player) self;
        if (player.level().isClientSide()) {
            return;
        }
        if (!AlepbLivingEntityMixin.hasAnyAlepbArmor(player)) {
            return;
        }
        cir.setReturnValue(false);
        cir.cancel();
    }

    private static boolean hasAnyAlepbArmor(Player player) {
        return AlepbLivingEntityMixin.isAlepbArmor(player.getItemBySlot(EquipmentSlot.HEAD))
                || AlepbLivingEntityMixin.isAlepbArmor(player.getItemBySlot(EquipmentSlot.CHEST))
                || AlepbLivingEntityMixin.isAlepbArmor(player.getItemBySlot(EquipmentSlot.LEGS))
                || AlepbLivingEntityMixin.isAlepbArmor(player.getItemBySlot(EquipmentSlot.FEET));
    }

    private static boolean isAlepbArmor(ItemStack stack) {
        return stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_HELMET.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_CHESTPLATE.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_LEGGINGS.get())
                || stack.is((Item) ModItems.MANBO_INFINITY_ALEPB_BOOTS.get());
    }
}
