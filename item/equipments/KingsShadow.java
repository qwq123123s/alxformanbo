/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ArmorItem
 *  net.minecraft.world.item.ArmorItem$Type
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.equipments;

import com.manbo.v2c.item.equipments.UpgradeArmorMaterial;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class KingsShadow
extends ArmorItem {
    public KingsShadow() {
        super(UpgradeArmorMaterial.UPGRADE_INFINITY, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().rarity(Rarity.EPIC));
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity instanceof Player && !stack.isEnchanted()) {
            stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 10);
            stack.enchant(Enchantments.FIRE_PROTECTION, 10);
            stack.enchant(Enchantments.PROJECTILE_PROTECTION, 10);
            stack.enchant(Enchantments.THORNS, 10);
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        String[] lines;
        String text = Component.translatable((String)"tooltip.manbov2c.kings_shadow").getString();
        for (String line : lines = text.split("\n")) {
            tooltipComponents.add((Component)Component.literal((String)line));
        }
    }
}

