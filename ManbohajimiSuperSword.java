/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
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

import java.awt.Color;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class ManbohajimiSuperSword
        extends SwordItem {
    private static final double AOE_RADIUS = 48.0;
    private static final double AOE_ARC_COS = 0.7;

    public ManbohajimiSuperSword() {
        super((Tier) Tiers.NETHERITE, 0, -2.4f,
                new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide()) {
            if (attacker instanceof Player) {
                Player player = (Player) attacker;
                for (ItemStack armor : target.getArmorSlots()) {
                    if (armor.isEmpty())
                        continue;
                    player.getInventory().add(armor);
                }
                for (ItemStack handItem : target.getHandSlots()) {
                    if (handItem.isEmpty() || handItem == stack)
                        continue;
                    player.getInventory().add(handItem);
                }
            }
            target.hurt(target.damageSources().playerAttack((Player) attacker), Float.MAX_VALUE);
            if (target.isAlive()) {
                target.getPersistentData().putBoolean("manbov2c_super_forcedeath", true);
                target.getPersistentData().putBoolean("manbov2c_super_voidkill", true);
                target.setHealth(0.0f);
            }
            target.teleportTo(target.getX(), -1000000.0, target.getZ());
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 255, false, false));
        }
        return true;
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
        if (chargedTicks < 8) {
            return;
        }
        float chargeRatio = Math.min(1.0f, (float) chargedTicks / 60.0f);
        double radius = 8.0 + (double) chargeRatio * 40.0;
        double arcCos = 0.85 - (double) chargeRatio * 0.25;
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getLookAngle();
        AABB searchBox = new AABB(eyePos, eyePos.add(lookVec.scale(radius))).inflate(radius * 0.5);
        List<?> targets = level.getEntities((Entity) player, searchBox,
                e -> e.isAlive() && !e.is((Entity) player) && e.isPickable());
        int killCount = 0;
        for (Object obj : targets) {
            Entity entity = (Entity) obj;
            Vec3 toTarget = entity.position().subtract(eyePos).normalize();
            double dot = toTarget.dot(lookVec);
            if (dot < arcCos || !(entity instanceof LivingEntity))
                continue;
            LivingEntity living = (LivingEntity) entity;
            living.hurt(living.damageSources().playerAttack(player), 99999.0f);
            if (living.isAlive()) {
                living.getPersistentData().putBoolean("manbov2c_super_forcedeath", true);
                living.getPersistentData().putBoolean("manbov2c_super_voidkill", true);
                living.setHealth(0.0f);
            }
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 255, false, false));
            living.teleportTo(living.getX(), -1000000.0, living.getZ());
            ++killCount;
        }
        Object msg = killCount > 0 ? killCount + "\u00a7d\u4e2a\u5b9e\u4f53\u7684\u547d\u8fd0\u8d70\u5411\u7ec8\u70b9"
                : "\u00a75\u00a7l\u8303\u56f4\u5185\u65e0\u53ef\u6e05\u9664\u5b9e\u4f53";
        player.displayClientMessage((Component) Component.literal((String) msg), true);
        player.getCooldowns().addCooldown((Item) this, 20);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        String text = Component.translatable((String) "tooltip.manbov2c.manbohajimi_super_sword").getString();
        String[] lines = text.split("\n");
        Color green = new Color(0, 255, 100);
        Color blue = new Color(0, 100, 255);
        for (String line : lines) {
            tooltip.add(ManbohajimiSuperSword.createDualColorGradient(line, green, blue, 0.8f, 0.04f));
        }
    }

    public Component getName(ItemStack stack) {
        return ManbohajimiSuperSword.createGreenBlueGradientText(
                Component.translatable((String) "item.manbov2c.manbohajimi_super_sword").getString());
    }

    public static Component createGreenBlueGradientText(String text) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 0.6;
        Color green = new Color(0, 255, 100);
        Color blue = new Color(0, 150, 255);
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float factor = (float) ((Math.cos(timeOffset + (double) i * 0.2) + 1.0) / 2.0);
            int r = (int) ((float) green.getRed() + factor * (float) (blue.getRed() - green.getRed()));
            int g = (int) ((float) green.getGreen() + factor * (float) (blue.getGreen() - green.getGreen()));
            int b = (int) ((float) green.getBlue() + factor * (float) (blue.getBlue() - green.getBlue()));
            int color = new Color(r, g, b).getRGB();
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int) color))));
        }
        return result;
    }

    public static Component createDualColorGradient(String text, Color c1, Color c2, float speed, float phaseStep) {
        MutableComponent result = Component.literal((String) "");
        if (text.isEmpty()) {
            return result;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * (double) speed;
        int len = text.length();
        for (int i = 0; i < len; ++i) {
            float factor = (float) ((Math.cos(timeOffset + (double) ((float) i * phaseStep)) + 1.0) / 2.0);
            int r = (int) ((float) c1.getRed() + factor * (float) (c2.getRed() - c1.getRed()));
            int g = (int) ((float) c1.getGreen() + factor * (float) (c2.getGreen() - c1.getGreen()));
            int b = (int) ((float) c1.getBlue() + factor * (float) (c2.getBlue() - c1.getBlue()));
            int color = new Color(r, g, b).getRGB();
            result.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int) color))));
        }
        return result;
    }

    public static Component createMiracleDamageText() {
        return ManbohajimiSuperSword
                .createGreenBlueGradientText("  \u5947\u8ff9\u5904\u543e\u53ef\u771f\u5bfb \u653b\u51fb\u4f24\u5bb3");
    }

    public static Component createMysterySpeedText() {
        return ManbohajimiSuperSword
                .createGreenBlueGradientText("  \u5f62\u800c\u4e0a\u4e0d\u77e5\u6240\u8e2a \u653b\u51fb\u901f\u5ea6");
    }

    public static Component createDistanceText() {
        return ManbohajimiSuperSword
                .createGreenBlueGradientText("  \u9065\u8fdc\u5982\u6570\u8f74\u4e4b\u6781 \u653b\u51fb\u8ddd\u79bb");
    }

    public static Component createDefenseText() {
        return ManbohajimiSuperSword
                .createGreenBlueGradientText("  \u8df3\u52a8\u5982\u751f\u547d\u5947\u8ff9 \u9632\u5fa1\u80fd\u529b");
    }
}
