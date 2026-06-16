/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.food.FoodProperties$Builder
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Rarity
 */
package com.manbo.v2c.item.foods;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RoastedHorseshoe
extends Item {
    public RoastedHorseshoe() {
        super(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(10.0f).alwaysEat().effect(() -> new MobEffectInstance(MobEffects.JUMP, 6000, 1), 1.0f).effect(() -> new MobEffectInstance(MobEffects.SATURATION, 6000, 7), 1.0f).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 3), 1.0f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 6000, 1), 1.0f).effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 6000, 7), 1.0f).effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 6000, 9), 1.0f).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 3), 1.0f).build()).rarity(Rarity.EPIC).fireResistant());
    }
}

