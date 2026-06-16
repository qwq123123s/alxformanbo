/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.manbo.v2c.mixin;

import com.manbo.v2c.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractFurnaceBlockEntity.class})
public class AbstractFurnaceBlockEntityMixin {
    @Inject(method={"burn"}, at={@At(value="RETURN")}, cancellable=true)
    private void onBurn(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize, CallbackInfoReturnable<Boolean> cir) {
        if (!((Boolean)cir.getReturnValue()).booleanValue()) {
            return;
        }
        ItemStack input = (ItemStack)items.get(0);
        ItemStack result = (ItemStack)items.get(2);
        if (!input.is((Item)ModItems.SHUANGSHENG_MATI.get())) {
            return;
        }
        if (!result.is((Item)ModItems.ROASTED_HORSESHOE.get())) {
            return;
        }
        result.setCount(64);
    }
}

