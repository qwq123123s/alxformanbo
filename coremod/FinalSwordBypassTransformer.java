/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.modlauncher.api.ITransformer
 *  cpw.mods.modlauncher.api.ITransformer$Target
 *  cpw.mods.modlauncher.api.ITransformerVotingContext
 *  cpw.mods.modlauncher.api.TransformerVoteResult
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package com.manbo.v2c.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import java.util.Set;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FinalSwordBypassTransformer
implements ITransformer<ClassNode> {
    private static final String ENTITY_CLASS = "net.minecraft.world.entity.Entity";
    private static final String LIVING_ENTITY_CLASS = "net.minecraft.world.entity.LivingEntity";
    private static final String HOOKS_CLASS = "com/manbo/v2s/coremod/FinalSwordBypassHooks";

    public ClassNode transform(ClassNode input, ITransformerVotingContext context) {
        String className = input.name.replace('/', '.');
        for (MethodNode method : input.methods) {
            if (ENTITY_CLASS.equals(className)) {
                if (!"remove".equals(method.name) || !"(Lnet/minecraft/world/entity/Entity$RemovalReason;)V".equals(method.desc)) continue;
                this.replaceMethodBody(method, () -> {
                    InsnList list = new InsnList();
                    list.add((AbstractInsnNode)new VarInsnNode(25, 0));
                    list.add((AbstractInsnNode)new MethodInsnNode(184, HOOKS_CLASS, "onEntityRemove", "(Lnet/minecraft/world/entity/Entity;)V", false));
                    list.add((AbstractInsnNode)new InsnNode(177));
                    return list;
                });
                continue;
            }
            if (!LIVING_ENTITY_CLASS.equals(className)) continue;
            if ("hurt".equals(method.name) && "(Lnet/minecraft/world/damagesource/DamageSource;F)Z".equals(method.desc)) {
                this.replaceMethodBody(method, () -> {
                    InsnList list = new InsnList();
                    list.add((AbstractInsnNode)new VarInsnNode(25, 0));
                    list.add((AbstractInsnNode)new VarInsnNode(25, 1));
                    list.add((AbstractInsnNode)new VarInsnNode(23, 2));
                    list.add((AbstractInsnNode)new MethodInsnNode(184, HOOKS_CLASS, "onLivingHurt", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)Z", false));
                    list.add((AbstractInsnNode)new InsnNode(172));
                    return list;
                });
                continue;
            }
            if ("die".equals(method.name) && "(Lnet/minecraft/world/damagesource/DamageSource;)V".equals(method.desc)) {
                this.replaceMethodBody(method, () -> {
                    InsnList list = new InsnList();
                    list.add((AbstractInsnNode)new VarInsnNode(25, 0));
                    list.add((AbstractInsnNode)new MethodInsnNode(184, HOOKS_CLASS, "onLivingKill", "(Lnet/minecraft/world/entity/LivingEntity;)V", false));
                    list.add((AbstractInsnNode)new InsnNode(177));
                    return list;
                });
                continue;
            }
            if (!"setHealth".equals(method.name) || !"(F)V".equals(method.desc)) continue;
            this.replaceMethodBody(method, () -> {
                InsnList list = new InsnList();
                list.add((AbstractInsnNode)new VarInsnNode(25, 0));
                list.add((AbstractInsnNode)new VarInsnNode(23, 1));
                list.add((AbstractInsnNode)new MethodInsnNode(184, HOOKS_CLASS, "onSetHealth", "(Lnet/minecraft/world/entity/LivingEntity;F)V", false));
                list.add((AbstractInsnNode)new InsnNode(177));
                return list;
            });
        }
        return input;
    }

    private void replaceMethodBody(MethodNode method, InsnSupplier supplier) {
        InsnList instructions = method.instructions;
        instructions.clear();
        instructions.add(supplier.get());
    }

    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    public Set<ITransformer.Target> targets() {
        return Set.of(ITransformer.Target.targetClass((String)ENTITY_CLASS), ITransformer.Target.targetClass((String)LIVING_ENTITY_CLASS));
    }

    public String[] labels() {
        return new String[]{"manbov2c-final-sword-bypass"};
    }

    @FunctionalInterface
    private static interface InsnSupplier {
        public InsnList get();
    }
}

