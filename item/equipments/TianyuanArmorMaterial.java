/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.ArmorMaterial
 *  net.minecraft.world.item.crafting.Ingredient
 */
package com.manbo.v2c.item.equipments;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class TianyuanArmorMaterial
        implements ArmorMaterial {
    public static final ArmorMaterial TIANYUAN = new TianyuanArmorMaterial();
    private static final int[] DURABILITY_PER_TYPE = new int[] { 13, 15, 16, 11 };
    private static final int DURABILITY_MULTIPLIER = 37;

    public int getDurabilityForType(ArmorItem.Type type) {
        return 37 * DURABILITY_PER_TYPE[type.ordinal()];
    }

    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 9;
            case CHESTPLATE -> 24;
            case LEGGINGS -> 18;
            case BOOTS -> 9;
        };
    }

    public int getEnchantmentValue() {
        return 30;
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    public String getName() {
        return "netherite";
    }

    public float getToughness() {
        return 9.0f;
    }

    public float getKnockbackResistance() {
        return 3.0f;
    }
}
