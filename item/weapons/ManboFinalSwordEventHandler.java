/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.ModSounds;
import com.manbo.v2c.coremod.FinalSwordBypassHooks;
import com.manbo.v2c.network.ManboFinalSwordHitPacket;
import com.manbo.v2c.network.NetworkHandler;
import com.manbo.v2c.util.ManboDeathList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "manbov2c", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManboFinalSwordEventHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity directEntity = event.getSource().getDirectEntity();
        if (!(directEntity instanceof Player)) {
            return;
        }
        Player player = (Player) directEntity;
        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is((Item) ModItems.MANBO_FINAL_SWORD.get())) {
            return;
        }
        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }
        LivingEntity target = event.getEntity();
        level.playSound(null, target.getX(), target.getY(), target.getZ(), ModSounds.getRandomHitSound(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
        target.getPersistentData().putBoolean("manbov2c_final_bypasshurt", true);
        ManboFinalSwordEventHandler.forceKillEntity((Entity) target, player, level);
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            NetworkHandler.sendToPlayer(new ManboFinalSwordHitPacket(), serverPlayer);
        }
        event.setAmount(Float.MAX_VALUE);
    }

    public static void onCoremodKill(LivingEntity target, Player player) {
        if (target == null || player == null) {
            return;
        }
        player.level().playSound(null, target.getX(), target.getY(), target.getZ(), ModSounds.getRandomHitSound(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            NetworkHandler.sendToPlayer(new ManboFinalSwordHitPacket(), serverPlayer);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void forceKillEntity(Entity target, Player player, Level level) {
        if (!(target instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity) target;
        if (level.isClientSide()) {
            return;
        }
        if (living.getPersistentData().getBoolean("manbov2c_final_kill_guard")) {
            return;
        }
        living.getPersistentData().putBoolean("manbov2c_final_kill_guard", true);
        ManboDeathList.addDeath(target);
        try {
            FinalSwordBypassHooks.setBypassActive(true);
            try {
                living.kill();
            } catch (Exception exception) {
                // empty catch block
            }
            try {
                living.remove(Entity.RemovalReason.KILLED);
            } catch (Exception exception) {
                // empty catch block
            }
            try {
                Method dieMethod = LivingEntity.class.getDeclaredMethod("die", DamageSource.class);
                dieMethod.setAccessible(true);
                dieMethod.invoke(living, living.damageSources().genericKill());
            } catch (Exception dieMethod) {
                // empty catch block
            }
            try {
                Field healthField = LivingEntity.class.getDeclaredField("health");
                healthField.setAccessible(true);
                healthField.setFloat(living, -3.4028235E38f);
            } catch (Exception exception) {
                // empty catch block
            }
            living.setPos(Double.NaN, Double.NaN, Double.NaN);
            try {
                living.removeAllEffects();
            } catch (Exception exception) {
                // empty catch block
            }
            try {
                living.getPersistentData().remove("manbov2c_final_kill_guard");
            } catch (Exception exception) {
                // empty catch block
            }
            try {
                living.discard();
            } catch (Exception exception) {
                // empty catch block
            }
        } finally {
            FinalSwordBypassHooks.setBypassActive(false);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (!stack.is((Item) ModItems.MANBO_FINAL_SWORD.get())) {
            return;
        }
        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }
        double range = 15.0;
        double halfAngleDeg = 30.0;
        double angleCos = Math.cos(Math.toRadians(halfAngleDeg));
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookDir = player.getLookAngle().normalize();
        AABB searchBox = player.getBoundingBox().inflate(range);
        int hitCount = 0;
        for (Entity target : level.getEntities((Entity) player, searchBox)) {
            Vec3 dirToTarget;
            double dot;
            Vec3 toTarget;
            double distance;
            if (target == player || !(target instanceof LivingEntity)
                    || (distance = (toTarget = target.position().subtract(eyePos)).length()) > range || distance < 0.3
                    || (dot = lookDir.dot(dirToTarget = toTarget.normalize())) < angleCos)
                continue;
            target.getPersistentData().putBoolean("manbov2c_final_bypasshurt", true);
            ManboFinalSwordEventHandler.forceKillEntity(target, player, level);
            level.playSound(null, target.getX(), target.getY(), target.getZ(), ModSounds.getRandomHitSound(),
                    SoundSource.PLAYERS, 1.0f, 0.8f + level.random.nextFloat() * 0.4f);
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                NetworkHandler.sendToPlayer(new ManboFinalSwordHitPacket(), serverPlayer);
            }
            ++hitCount;
        }
        if (hitCount > 0) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.getRandomHitSound(),
                    SoundSource.PLAYERS, 1.5f, 1.0f);
        }
        event.setCanceled(true);
    }
}
