/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.Attributes
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
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

public class ManboVoidSword
        extends SwordItem {
    private static final Random RANDOM = new Random();
    private static final char[] GLITCH_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=[]{}|;':\",./<>?~`"
            .toCharArray();

    public ManboVoidSword() {
        super((Tier) Tiers.NETHERITE, 6666, -2.4f,
                new Item.Properties().fireResistant().durability(Integer.MAX_VALUE).rarity(Rarity.EPIC));
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide()) {
            if (target instanceof Player) {
                return super.hurtEnemy(stack, target, attacker);
            }
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
            target.getPersistentData().putBoolean("manbovoid_kill", true);
            target.teleportTo(target.getX(), -1000000.0, target.getZ());
        }
        return super.hurtEnemy(stack, target, attacker);
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
        Vec3 endPos = eyePos.add(lookVec.scale(64.0));
        AABB searchBox = new AABB(eyePos, endPos).inflate(3.0);
        List<?> entities = level.getEntities((Entity) player, searchBox,
                e -> e.isAlive() && !e.is((Entity) player) && e.isPickable());
        int resetCount = 0;
        for (Object obj : entities) {
            Entity e2 = (Entity) obj;
            Vec3 toTarget = e2.position().subtract(eyePos).normalize();
            if (toTarget.dot(lookVec) < 0.85)
                continue;
            this.resetEntityNBT(e2);
            ++resetCount;
        }
        if (resetCount > 0) {
            player.displayClientMessage(
                    (Component) Component.literal(
                            (String) ("\u00a75\u00a7l\u5df2\u4f7f " + resetCount + " \u4e2a\u6570\u636e\u635f\u574f")),
                    true);
        } else {
            player.displayClientMessage((Component) Component.literal((String) "\u00a75\u00a7l\u65e0\u53d8\u91cf"),
                    true);
        }
        player.getCooldowns().addCooldown((Item) this, 20);
    }

    private void resetEntityNBT(Entity entity) {
        CompoundTag empty = new CompoundTag();
        CompoundTag original = entity.saveWithoutId(new CompoundTag());
        if (original.contains("Pos")) {
            empty.put("Pos", (Tag) original.getList("Pos", 6));
        }
        if (original.contains("UUID")) {
            empty.putUUID("UUID", original.getUUID("UUID"));
        }
        if (original.contains("id")) {
            empty.putString("id", original.getString("id"));
        }
        if (original.contains("Motion")) {
            empty.put("Motion", (Tag) original.getList("Motion", 6));
        }
        if (original.contains("Rotation")) {
            empty.put("Rotation", (Tag) original.getList("Rotation", 5));
        }
        if (original.contains("WorldUUIDMost")) {
            empty.putLong("WorldUUIDMost", original.getLong("WorldUUIDMost"));
        }
        if (original.contains("WorldUUIDLeast")) {
            empty.putLong("WorldUUIDLeast", original.getLong("WorldUUIDLeast"));
        }
        entity.load(empty);
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            AttributeInstance maxHealthAttr = living.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                maxHealthAttr.setBaseValue(0.0);
            }
            living.setHealth(0.0f);
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public static Component createPurpleGrayGradientText(String text) {
        MutableComponent root = Component.literal((String) "");
        if (text.isEmpty()) {
            return root;
        }
        double timeOffset = (double) System.currentTimeMillis() / 1000.0 * 1.5;
        Color purple = new Color(180, 0, 255);
        Color gray = new Color(160, 160, 180);
        for (int i = 0; i < text.length(); ++i) {
            double angle = timeOffset + (double) ((float) i * 0.15f);
            float factor = (float) ((Math.sin(angle) + 1.0) / 2.0);
            int r = (int) ((float) purple.getRed() + factor * (float) (gray.getRed() - purple.getRed()));
            int g = (int) ((float) purple.getGreen() + factor * (float) (gray.getGreen() - purple.getGreen()));
            int b = (int) ((float) purple.getBlue() + factor * (float) (gray.getBlue() - purple.getBlue()));
            int color = new Color(r, g, b).getRGB();
            root.append((Component) Component.literal((String) String.valueOf(text.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb((int) color))));
        }
        return root;
    }

    public static Component createGlitchDamageText() {
        StringBuilder sb = new StringBuilder("  +");
        int len = 4 + RANDOM.nextInt(4);
        for (int i = 0; i < len; ++i) {
            sb.append(GLITCH_CHARS[RANDOM.nextInt(GLITCH_CHARS.length)]);
        }
        sb.append(" \u653b\u51fb\u4f24\u5bb3");
        return ManboVoidSword.createPurpleGrayGradientText(sb.toString());
    }

    public static Component createMysterySpeedText() {
        return ManboVoidSword.createPurpleGrayGradientText("  -\u672a\u89e3\u4e4b\u8c1c \u653b\u51fb\u901f\u5ea6");
    }

    public Component getName(ItemStack stack) {
        String baseName = Component.translatable((String) "item.manbov2c.manbo_void_sword").getString();
        return ManboVoidSword.createPurpleGrayGradientText(baseName);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
            TooltipFlag isAdvanced) {
        String[] lines;
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        String text = Component.translatable((String) "tooltip.manbov2c.manbo_void_sword").getString();
        for (String line : lines = text.split("\n")) {
            tooltipComponents.add(ManboVoidSword.createPurpleGrayGradientText(line));
        }
    }

    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
