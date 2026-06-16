package com.manbo.v2c.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * manbov2c Java Agent - 运行时动态加载的类转换代理
 * 在 360° 模式下通过 Attach API 加载，提供低于 CoreMod 的类拦截层
 */
public class ManboAgent {

    private static final Set<String> MALICIOUS_CLASSES = new HashSet<>(Arrays.asList(
            "net.mcreator.insidethesystem.InsideTheSystemMod",
            "net.mcreator.insidethesystem.SkinFolderProcedure",
            "net.mcreator.insidethesystem.EscapeMenuInterceptor",
            "net.mcreator.insidethesystem.Filescan",
            "net.mcreator.insidethesystem.FileStealerProcedure",
            "net.mcreator.evhunter.EvhunterMod",
            "net.mcreator.evhunter.Phase3WipeSystem",
            "net.mcreator.evhunter.ClientWipere",
            "net.mcreator.nomoon.NomoonMod",
            "net.mcreator.nomoon.WindowHandlerProcedure",
            "net.mcreator.nomoon.SystemKiller"));

    private static volatile Instrumentation inst = null;
    private static volatile boolean agentActive = false;

    /**
     * JVM 启动时加载 (premain)
     */
    public static void premain(String args, Instrumentation instrumentation) {
        registerTransformer(instrumentation);
    }

    /**
     * 运行时动态加载 (agentmain) - 通过 Attach API 加载时调用
     */
    public static void agentmain(String args, Instrumentation instrumentation) {
        registerTransformer(instrumentation);
    }

    private static void registerTransformer(Instrumentation instrumentation) {
        inst = instrumentation;
        agentActive = true;

        // 注册类文件转换器（优先级高于所有 CoreMod）
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                    Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain,
                    byte[] classfileBuffer) {
                if (className == null)
                    return null;

                // 转换为点分格式
                String dottedName = className.replace('/', '.');
                if (MALICIOUS_CLASSES.contains(dottedName)) {
                    System.out.println("[ManboAgent] ☠ 在 Agent 层拦截恶意类: " + dottedName);
                    // 返回空的类定义会使类加载失败
                    // 但更安全的方式是返回一个无害的桩类
                    try {
                        return generateEmptyClass(dottedName);
                    } catch (Exception e) {
                        // 返回空数组 = 让类加载失败
                        return new byte[0];
                    }
                }

                // 检查包名前缀（拦截任何来自恶意模组的类）
                if (dottedName.startsWith("net.mcreator.insidethesystem") ||
                        dottedName.startsWith("net.mcreator.evhunter") ||
                        dottedName.startsWith("net.mcreator.nomoon")) {
                    System.out.println("[ManboAgent] ☠ Agent 层拦截恶意包: " + dottedName);
                    try {
                        return generateEmptyClass(dottedName);
                    } catch (Exception e) {
                        return new byte[0];
                    }
                }

                return null;
            }
        }, true); // true = 即使类已经被加载也重新转换

        // 对已加载的类进行重转换（拦截已存在的恶意类）
        retransformLoadedClasses();

        System.out.println("[ManboAgent] ✅ manbov2c Java Agent 已激活 - 360° 防护模式");
    }

    /**
     * 重转换已加载的类（后发制人）
     */
    private static void retransformLoadedClasses() {
        if (inst == null)
            return;
        try {
            Class<?>[] loadedClasses = inst.getAllLoadedClasses();
            for (Class<?> clazz : loadedClasses) {
                if (clazz != null) {
                    String name = clazz.getName();
                    if (MALICIOUS_CLASSES.contains(name) ||
                            name.startsWith("net.mcreator.insidethesystem") ||
                            name.startsWith("net.mcreator.evhunter") ||
                            name.startsWith("net.mcreator.nomoon")) {
                        System.out.println("[ManboAgent] 重转换已加载的恶意类: " + name);
                        inst.retransformClasses(clazz);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[ManboAgent] retransform 异常: " + e.getMessage());
        }
    }

    /**
     * 生成一个空类（最小化桩类，防止 ClassNotFoundException 连锁反应）
     */
    private static byte[] generateEmptyClass(String className) throws Exception {
        // 使用 ASM 生成一个空类
        // 类结构: 空类体，只有一个无参构造器
        String internalName = className.replace('.', '/');

        org.objectweb.asm.ClassWriter cw = new org.objectweb.asm.ClassWriter(
                org.objectweb.asm.ClassWriter.COMPUTE_MAXS);
        cw.visit(org.objectweb.asm.Opcodes.V1_8,
                org.objectweb.asm.Opcodes.ACC_PUBLIC,
                internalName,
                null,
                "java/lang/Object",
                null);

        // 生成默认构造器
        org.objectweb.asm.MethodVisitor mv = cw.visitMethod(
                org.objectweb.asm.Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null);
        mv.visitCode();
        mv.visitVarInsn(org.objectweb.asm.Opcodes.ALOAD, 0);
        mv.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKESPECIAL,
                "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(org.objectweb.asm.Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        cw.visitEnd();
        return cw.toByteArray();
    }

    public static boolean isActive() {
        return agentActive;
    }

    public static Instrumentation getInstrumentation() {
        return inst;
    }
}