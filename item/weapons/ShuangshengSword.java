/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.item.SwordItem
 *  net.minecraft.world.item.Tier
 *  net.minecraft.world.item.Tiers
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package com.manbo.v2c.item.weapons;

import com.manbo.v2c.util.ColorUtils;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ShuangshengSword
        extends SwordItem {
    public ShuangshengSword() {
        super((Tier) Tiers.NETHERITE, 0, -2.4f,
                new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (level.isClientSide()) {
            return;
        }
        if (entity instanceof Player) {
            Player player = (Player) entity;
            CompoundTag persistData = player.getPersistentData();
            long lastUsed = persistData.getLong("shuangsheng_sword_totem_last");
            long l = persistData.getLong("shuangsheng_sword_totem_cd");
        }
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide()) {
            target.getPersistentData().putBoolean("manbov2c_shuangsheng_forcedeath", true);
            target.setHealth(0.0f);
            target.hurt(target.damageSources().genericKill(), Float.MAX_VALUE);
            if (target.isAlive()) {
                target.kill();
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (!(livingEntity instanceof Player)) {
            return;
        }
        Player player = (Player) livingEntity;
        if (level.isClientSide()) {
            return;
        }
        int useDuration = this.getUseDuration(stack);
        int chargedTicks = useDuration - timeCharged;
        if (chargedTicks < 10) {
            return;
        }
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getLookAngle();
        double range = 64.0;
        double radius = 3.0;
        Vec3 endPos = eyePos.add(lookVec.scale(range));
        AABB searchBox = new AABB(eyePos, endPos).inflate(radius);
        if (level.isClientSide()) {
            // empty if block
        }
        List<?> entities = level.getEntities((Entity) player, searchBox,
                e -> e.isAlive() && !e.is((Entity) player) && e.isPickable());
        int discarded = 0;
        for (Object obj : entities) {
            Entity target = (Entity) obj;
            double dist;
            Vec3 toTarget = target.position().subtract(eyePos).normalize();
            if (toTarget.dot(lookVec) < 0.85 || (dist = target.position().distanceTo(eyePos)) > range
                    || target instanceof Player)
                continue;
            target.discard();
            ++discarded;
        }
        if (discarded > 0) {
            player.displayClientMessage(
                    (Component) Component
                            .literal((String) ("\u00a7f\u6e6e\u706d\u4e86 " + discarded + " \u4e2a\u5b9e\u4f53")),
                    true);
        }
        player.getCooldowns().addCooldown((Item) this, 20);
    }

    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String) "item.manbov2c.shuangsheng_sword").getString();
        return ColorUtils.createGoldSilverGradientText(baseName);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
            TooltipFlag isAdvanced) {
        String[] lines;
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String) "tooltip.manbov2c.shuangsheng_sword").getString();
        for (String line : lines = text.split("\n")) {
            tooltipComponents.add((Component) ColorUtils.createGoldSilverGradientText(line));
        }
    }

    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
