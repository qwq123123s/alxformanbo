package com.manbo.v2c.server;

import com.manbo.v2c.agent.AgentLoader;
import com.manbo.v2c.client.defense.HorrorDefenseSystem;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 防御中枢控制器 ── 诗宝之爱防御体系
 * <p>
 * 防御等级:
 * - 标准模式（始终有效）: CoreMod 字节码中和 + ModKiller 检测 + ModFileGuard 文件守护 + BsodGuard
 * 命令防护
 * - 360模式（需诗宝之爱授权）: 额外启用 Java Agent / JVMTI / HookJS 等深度挂钩技术
 * <p>
 * 360模式提供:
 * 1. Java Instrumentation Agent — JVM 级 ClassFileTransformer，可在类重加载时拦截
 * 2. JVMTI Agent (native) — 原生层 API 调用拦截
 * 3. Runtime.exec 深度挂钩 — 从 JVM 到 OS 级别的命令执行拦截
 * 4. sun.misc.Unsafe 操作全面拦截
 */
public class DefenseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefenseController.class);

    // ======================================================================
    // 诗宝之爱 — 360 模式开关
    // 设为 true 启用全部核武器级防护手段
    // ======================================================================
    private static volatile boolean shibaoLoveActive = false;

    // 各子系统状态
    private static boolean coreModActive = false;
    private static boolean modKillerActive = false;
    private static boolean modFileGuardActive = false;
    private static boolean bsodGuardActive = false;
    private static boolean horrorDefenseActive = false;
    private static boolean antiKickActive = false;

    /**
     * 初始化防御体系
     * 在 mod 构造函数中调用
     */
    public static void init() {
        LOGGER.info("============================================");
        LOGGER.info("  诗宝之爱防御体系 v2c 正在启动...");
        LOGGER.info("============================================");

        // 激活 BsodGuard（安全管理器 + 进程看门狗）
        try {
            BsodGuard.init();
            bsodGuardActive = true;
            LOGGER.info("[防御中枢] ✓ BsodGuard 命令/进程防护已激活");
        } catch (Exception e) {
            LOGGER.warn("[防御中枢] ✗ BsodGuard 激活失败: {}", e.getMessage());
        }

        LOGGER.info("[防御中枢] ✓ CoreMod AntiMalwareTransformer 已就绪（字节码级中和）");
        coreModActive = true;

        LOGGER.info("[防御中枢] ✓ ModKiller 已就绪（模组级检测）");
        modKillerActive = true;

        // 启动 ModFileGuard（文件监控）
        startModFileGuard();

        // 激活防踢出保护（拦截 ClientboundDisconnectPacket）
        antiKickActive = true;
        LOGGER.info("[防御中枢] ✓ 防踢出保护已激活（玩家免疫其他模组的踢出）");

        // 激活指令守护（Mixin 字节码级 + Forge 事件级双重保险）
        CommandAndGamemodeProtector.activateCommandGuard();

        LOGGER.info("============================================");
        if (shibaoLoveActive) {
            LOGGER.info("  防御等级: 360° 完全体（诗宝之爱已激活）");
        } else {
            LOGGER.info("  防御等级: 标准模式");
            LOGGER.info("  提示: 开启 360 模式需激活诗宝之爱");
        }
        LOGGER.info("============================================");
    }

    /**
     * 激活诗宝之爱 360 模式
     * 启用 Agent/JVMTI/HookJS 等核武器级防御
     */
    public static void activateShibaoLove() {
        if (shibaoLoveActive)
            return;

        shibaoLoveActive = true;
        LOGGER.info("[防御中枢] ★★★★★ 诗宝之爱已激活 — 360 模式启动 ★★★★★");

        // ========== 360 模式额外防护 ==========

        // 1. 尝试加载 Java Agent（Instrumentation 级类拦截）
        LOGGER.info("[防御中枢] ▶ 正在加载 Java Agent...");
        try {
            if (AgentLoader.tryLoadAgent()) {
                LOGGER.info("[防御中枢] ✓ Java Agent 加载成功 - Agent 级类转换已激活");
            } else {
                LOGGER.warn("[防御中枢] ⚠ Java Agent 加载失败，Agent 级防护不可用");
            }
        } catch (Exception e) {
            LOGGER.warn("[防御中枢] ✗ Java Agent 异常: {}", e.getMessage());
        }

        // 2. 尝试加载 JVMTI 原生 Agent（如有编译好的 DLL）
        LOGGER.info("[防御中枢] ▶ 正在尝试加载 JVMTI Agent...");
        try {
            if (tryLoadJvmtiAgent()) {
                LOGGER.info("[防御中枢] ✓ JVMTI Agent 加载成功 - 原生层防护已激活");
            } else {
                LOGGER.info("[防御中枢] - JVMTI Agent 未安装（需要编译 AntiMalwareAgent.c）");
            }
        } catch (Exception e) {
            LOGGER.warn("[防御中枢] ✗ JVMTI Agent 异常: {}", e.getMessage());
        }

        // 3. 注册终止保护
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("[防御中枢] 系统终止，防御体系关闭");
        }, "manbov2c-shutdown-hook"));

        LOGGER.info("[防御中枢] ✓ 360 模式所有增强防护已就绪");
    }

    /**
     * 尝试加载 JVMTI 原生 Agent DLL
     */
    private static boolean tryLoadJvmtiAgent() {
        // 搜索路径: run/ 目录下预编译好的 DLL
        String[] searchPaths = {
                "AntiMalwareAgent.dll",
                "run/AntiMalwareAgent.dll",
                "native/AntiMalwareAgent.dll",
                System.getProperty("java.io.tmpdir") + "manbov2c" + File.separator + "AntiMalwareAgent.dll"
        };

        for (String path : searchPaths) {
            File dllFile = new File(path);
            if (dllFile.exists()) {
                try {
                    System.load(dllFile.getAbsolutePath());
                    LOGGER.info("[防御中枢] JVMTI Agent DLL 已加载: {}", dllFile.getAbsolutePath());
                    return true;
                } catch (Exception e) {
                    LOGGER.warn("[防御中枢] JVMTI Agent DLL 加载失败: {}", e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 启动 ModFileGuard 文件监控
     */
    private static void startModFileGuard() {
        try {
            // 查找 mods 目录
            String modsDir = null;

            // 尝试从 FMLLoader 获取 mods 目录
            try {
                // Forge 1.20.1: FMLLoader.getLoadingModList() 但 mods 目录通常由 launch 参数指定
                File currentDir = new File(".");
                String[] searchPaths = {
                        "mods",
                        FMLLoader.getGamePath().resolve("mods").toString(),
                        new File(".").getAbsolutePath() + File.separator + "mods"
                };

                for (String path : searchPaths) {
                    File dir = new File(path);
                    if (dir.exists() && dir.isDirectory()) {
                        modsDir = dir.getAbsolutePath();
                        break;
                    }
                }
            } catch (Exception e) {
                // fallback
                File defaultMods = new File("mods");
                if (defaultMods.exists() && defaultMods.isDirectory()) {
                    modsDir = defaultMods.getAbsolutePath();
                }
            }

            if (modsDir != null) {
                ModFileGuard.start(modsDir);
                ModFileGuard.protectSelf();
                modFileGuardActive = true;
                LOGGER.info("[防御中枢] ✓ ModFileGuard 文件守护已激活，监控目录: {}", modsDir);
            } else {
                LOGGER.warn("[防御中枢] ✗ ModFileGuard 无法找到 mods 目录，跳过文件监控");
            }
        } catch (Exception e) {
            LOGGER.warn("[防御中枢] ✗ ModFileGuard 启动失败: {}", e.getMessage());
        }
    }

    // ======================================================================
    // 状态查询
    // ======================================================================

    public static boolean isShibaoLoveActive() {
        return shibaoLoveActive;
    }

    public static boolean isCoreModActive() {
        return coreModActive;
    }

    public static boolean isModKillerActive() {
        return modKillerActive;
    }

    public static boolean isModFileGuardActive() {
        return modFileGuardActive;
    }

    public static boolean isBsodGuardActive() {
        return bsodGuardActive;
    }

    public static boolean isAntiKickActive() {
        return antiKickActive;
    }

    /**
     * 获取防御状态报告
     */
    public static String getStatusReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("§6=== 诗宝之爱防御体系 v2c ===\n");
        sb.append("§e防御等级: ").append(shibaoLoveActive ? "§c360° 完全体" : "§a标准模式").append("\n");
        sb.append("§eAntiMalwareTransformer: ").append(coreModActive ? "§a✓ 激活" : "§c✗ 未激活").append("\n");
        sb.append("§eModKiller: ").append(modKillerActive ? "§a✓ 激活" : "§c✗ 未激活").append("\n");
        sb.append("§eModFileGuard: ").append(modFileGuardActive ? "§a✓ 激活" : "§c✗ 未激活").append("\n");
        sb.append("§eBsodGuard: ").append(bsodGuardActive ? "§a✓ 激活" : "§c✗ 未激活").append("\n");
        sb.append("§eHorrorDefenseSystem: ").append(horrorDefenseActive ? "§a✓ 激活" : "§c✗ 未激活").append("\n");
        sb.append("§eAntiKickProtection: ").append(antiKickActive ? "§a✓ 激活" : "§c✗ 未激活").append("\n");
        sb.append("§eCommandGuard: ").append(CommandAndGamemodeProtector.isCommandGuardActive() ? "§a✓ 激活" : "§c✗ 未激活")
                .append("\n");
        if (shibaoLoveActive) {
            sb.append("§c★ 诗宝之爱 360 模式全开 ★\n");
        }
        return sb.toString();
    }
}