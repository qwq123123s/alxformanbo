/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.modlauncher.api.ITransformer
 *  cpw.mods.modlauncher.api.ITransformer$Target
 *  cpw.mods.modlauncher.api.ITransformerVotingContext
 *  cpw.mods.modlauncher.api.TransformerVoteResult
 *  org.objectweb.asm.Label
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package com.manbo.v2c.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import java.util.Set;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class UnknownArmorTransformer
implements ITransformer<ClassNode> {
    private static final String TARGET_CLASS = "net.minecraft.world.entity.LivingEntity";
    private static final String HELPER_CLASS = "com/manbo/v2s/coremod/UnknownArmorHelper";
    private static final String FINAL_SWORD_HELPER_CLASS = "com/manbo/v2s/coremod/FinalSwordCoreModHelper";

    public ClassNode transform(ClassNode input, ITransformerVotingContext context) {
        for (MethodNode method : input.methods) {
            if ("hurt".equals(method.name) && method.desc.contains("DamageSource") && method.desc.endsWith("Z")) {
                this.injectFinalSwordHurtCheck(method);
                this.injectDamageSourceCheck(method);
            }
            if (!"tick".equals(method.name) || !"()V".equals(method.desc)) continue;
            this.injectFinalSwordTickCheck(method);
        }
        return input;
    }

    private void injectFinalSwordHurtCheck(MethodNode method) {
        InsnList instructions = method.instructions;
        if (instructions.size() == 0) {
            return;
        }
        InsnList inject = new InsnList();
        inject.add((AbstractInsnNode)new VarInsnNode(25, 0));
        inject.add((AbstractInsnNode)new VarInsnNode(25, 1));
        inject.add((AbstractInsnNode)new MethodInsnNode(184, FINAL_SWORD_HELPER_CLASS, "tryKillEntity", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;)Z", false));
        LabelNode jumpToNext = new LabelNode(new Label());
        inject.add((AbstractInsnNode)new JumpInsnNode(153, jumpToNext));
        inject.add((AbstractInsnNode)new InsnNode(4));
        inject.add((AbstractInsnNode)new InsnNode(172));
        inject.add((AbstractInsnNode)jumpToNext);
        instructions.insert(inject);
    }

    private void injectFinalSwordTickCheck(MethodNode method) {
        InsnList instructions = method.instructions;
        if (instructions.size() == 0) {
            return;
        }
        InsnList inject = new InsnList();
        inject.add((AbstractInsnNode)new VarInsnNode(25, 0));
        inject.add((AbstractInsnNode)new InsnNode(1));
        inject.add((AbstractInsnNode)new MethodInsnNode(184, FINAL_SWORD_HELPER_CLASS, "tryKillEntity", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;)Z", false));
        LabelNode jumpToOriginal = new LabelNode(new Label());
        inject.add((AbstractInsnNode)new JumpInsnNode(153, jumpToOriginal));
        inject.add((AbstractInsnNode)new InsnNode(177));
        inject.add((AbstractInsnNode)jumpToOriginal);
        instructions.insert(inject);
    }

    private void injectDamageSourceCheck(MethodNode method) {
        InsnList instructions = method.instructions;
        if (instructions.size() == 0) {
            return;
        }
        InsnList inject = new InsnList();
        inject.add((AbstractInsnNode)new VarInsnNode(25, 0));
        inject.add((AbstractInsnNode)new VarInsnNode(25, 1));
        inject.add((AbstractInsnNode)new VarInsnNode(23, 2));
        inject.add((AbstractInsnNode)new MethodInsnNode(184, HELPER_CLASS, "shouldCancelDamage", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)Z", false));
        LabelNode jumpToOriginal = new LabelNode(new Label());
        inject.add((AbstractInsnNode)new JumpInsnNode(153, jumpToOriginal));
        inject.add((AbstractInsnNode)new InsnNode(3));
        inject.add((AbstractInsnNode)new InsnNode(172));
        inject.add((AbstractInsnNode)jumpToOriginal);
        instructions.insert(inject);
    }

    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    public Set<ITransformer.Target> targets() {
        return Set.of(ITransformer.Target.targetClass((String)TARGET_CLASS));
    }

    public String[] labels() {
        return new String[]{"manbov2c-unknown-armor-and-final-sword"};
    }
}

