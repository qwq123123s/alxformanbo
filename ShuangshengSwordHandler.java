/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="manbov2c")
public class ShuangshengSwordHandler {
    private static final long TOTEM_COOLDOWN_TICKS = 360L;

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        long lastUsed;
        LivingEntity livingEntity = event.getEntity();
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player)livingEntity;
        if (player.level().isClientSide()) {
            return;
        }
        boolean hasSword = false;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.is((Item)ModItems.SHUANGSHENG_SWORD.get())) continue;
            hasSword = true;
            break;
        }
        if (!hasSword && player.getOffhandItem().is((Item)ModItems.SHUANGSHENG_SWORD.get())) {
            hasSword = true;
        }
        if (!hasSword) {
            return;
        }
        CompoundTag persistData = player.getPersistentData();
        long gameTime = player.level().getGameTime();
        if (gameTime - (lastUsed = persistData.getLong("shuangsheng_sword_totem_last")) < 360L) {
            long remainingTicks = 360L - (gameTime - lastUsed);
            float remainingSeconds = (float)remainingTicks / 20.0f;
            player.displayClientMessage((Component)Component.literal((String)("\u00a7c\u53cc\u751f\u53d9\u4e8b\u4fee\u6539\u51b7\u5374\u4e2d  \u00a7f\u5269\u4f59 " + String.format("%.1f", Float.valueOf(remainingSeconds)) + " \u79d2")), true);
            return;
        }
        event.setCanceled(true);
        persistData.putLong("shuangsheng_sword_totem_last", gameTime);
        player.setHealth(1.0f);
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        Level level = player.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            serverLevel.playSeededSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0f, 1.0f, player.getRandom().nextLong());
            serverLevel.sendParticles((ParticleOptions)ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY() + 1.0, player.getZ(), 80, 0.5, 1.0, 0.5, 0.5);
        }
        player.displayClientMessage((Component)Component.literal((String)"\u00a7d\u53cc\u751f\u4e4b\u529b \u00a7f\u3010\u6b7b\u4ea1\u3011\u7684\u53d9\u4e8b\u88ab\u62b9\u9664\u4e86"), true);
    }
}

