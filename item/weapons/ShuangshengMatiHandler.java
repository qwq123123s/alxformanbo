/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class ShuangshengMatiHandler {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();
            if (mainHand.is((Item)ModItems.SHUANGSHENG_MATI.get()) || offHand.is((Item)ModItems.SHUANGSHENG_MATI.get())) {
                float original = event.getAmount();
                float modified = original * 2.0f;
                if (modified < 10.0f) {
                    modified = 10.0f;
                }
                event.setAmount(modified);
            }
        }
    }
}

