/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.animal.Pig
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.TickEvent$PlayerTickEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingHurtEvent
 *  net.minecraftforge.event.entity.living.LivingKnockBackEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 */
package com.manbo.v2c;

import com.manbo.v2c.ModBlocks;
import com.manbo.v2c.ModItems;
import com.manbo.v2c.ModSounds;
import com.manbo.v2c.network.NetworkHandler;
import com.manbo.v2c.server.DefenseController;
import com.manbo.v2c.util.ColorUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = "manbov2c")
public class manbov2 {
    public static final String MOD_ID = "manbov2c";

    public manbov2() {
        // 初始化诗宝之爱防御体系
        DefenseController.init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        ModItems.TABS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModSounds.SOUND_EVENTS.register(bus);
        NetworkHandler.init();
        MinecraftForge.EVENT_BUS.register((Object) this);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag data = player.getPersistentData();
        boolean hasJoined = data.getBoolean("manbov2c_has_joined");
        if (hasJoined) {
            player.sendSystemMessage((Component) ColorUtils.createPurplePinkBlueGreenYellowGradientText(
                    "\u4e16\u754c\u518d\u6b21\u5f00\u542f\uff0c\u65f6\u95f4\u518d\u6b21\u6d41\u52a8", 1.0f, 0.6f,
                    0.0f));
        } else {
            data.putBoolean("manbov2c_has_joined", true);
            MutableComponent msg = ColorUtils.createBluePurpleGreenGradientText(
                    "\u8bd7\u5b9d\u7684\u4e16\u754c\u5df2\u7ecf\u5f00\u542f\uff0c\u4e5f\u8bb8\uff0c\u8fd9\u4e2a\u4e16\u754c\u6b63\u7b49\u5f85\u4e00\u4e2a\u771f\u6b63\u7684");
            msg.append((Component) ColorUtils.createWobblingText("\u795e", 2.0f));
            player.sendSystemMessage((Component) msg);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        Entity entity = event.getSource().getDirectEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ItemStack weapon = player.getMainHandItem();
            if (weapon.is((Item) ModItems.MANBO_BASIC_SWORD.get())) {
                float targetHealth = event.getEntity().getHealth();
                event.setAmount(targetHealth * 2.0f);
            }
            if (weapon.is((Item) ModItems.MANBO_FEET.get())) {
                LivingEntity target = event.getEntity();
                Vec3 look = player.getLookAngle();
                target.setDeltaMovement(look.x * 18.0, 0.5, look.z * 18.0);
                target.hurtMarked = true;
                event.setAmount(0.0f);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        boolean hasSword;
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Player player = event.player;
        if (player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % 20 != 0) {
            return;
        }
        boolean bl = hasSword = player.getInventory().items.stream()
                .anyMatch(s -> s.is((Item) ModItems.MANBO_BASIC_SWORD.get()))
                || player.getInventory().armor.stream().anyMatch(s -> s.is((Item) ModItems.MANBO_BASIC_SWORD.get()))
                || player.getInventory().offhand.stream().anyMatch(s -> s.is((Item) ModItems.MANBO_BASIC_SWORD.get()));
        if (hasSword) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 1, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false, true));
        }
    }

    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            boolean hasStick;
            Player player = (Player) livingEntity;
            boolean bl = hasStick = player.getInventory().items.stream()
                    .anyMatch(s -> s.is((Item) ModItems.MANBO_STICK.get()))
                    || player.getInventory().armor.stream().anyMatch(s -> s.is((Item) ModItems.MANBO_STICK.get()))
                    || player.getInventory().offhand.stream().anyMatch(s -> s.is((Item) ModItems.MANBO_STICK.get()));
            if (hasStick) {
                event.setStrength(0.0f);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        Player player;
        ItemStack weapon;
        Entity entity = event.getSource().getDirectEntity();
        if (entity instanceof Player
                && (weapon = (player = (Player) entity).getMainHandItem()).is((Item) ModItems.PIGGOD.get())
                && event.getEntity() instanceof Pig) {
            player.displayClientMessage(
                    (Component) Component.literal((String) "dream\u8fd8\u6211\u5341\u4e07\u7f8e\u5200"), false);
        }
    }
}
