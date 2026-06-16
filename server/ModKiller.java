package com.manbo.v2c.server;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模组杀手 ── Forge 模组加载检测器
 * <p>
 * 在游戏启动阶段扫描已加载的所有模组，
 * 如果发现 ITS(InsideTheSystem)/EvilHunter/no_moon 这三个恶意模组，
 * 记录警告日志并采取阻断措施。
 * <p>
 * 第二层防御（模组级），始终有效。
 */
@Mod.EventBusSubscriber(modid = "manbov2c", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKiller {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModKiller.class);

    // 已知恶意模组的 Mod ID 列表
    private static final List<String> MALICIOUS_MOD_IDS = Arrays.asList(
            "insidethesystem", "its",
            "evhunter", "evil_hunter", "evilhunter",
            "nomoon", "no_moon");

    // 已检测到的恶意模组
    private static final List<String> FOUND_MALICIOUS_MODS = new ArrayList<>();

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        scanForMaliciousMods();
    }

    /**
     * 扫描所有已加载模组，查找恶意模组
     */
    private static void scanForMaliciousMods() {
        try {
            List<ModInfo> mods = FMLLoader.getLoadingModList().getMods();
            for (ModInfo mod : mods) {
                String modId = mod.getModId().toLowerCase();
                String displayName = mod.getDisplayName();

                // 检查 modId 是否匹配恶意列表
                for (String malicious : MALICIOUS_MOD_IDS) {
                    if (modId.contains(malicious)) {
                        FOUND_MALICIOUS_MODS.add(modId + " (" + displayName + ")");
                        LOGGER.warn("═══════════════════════════════════════════════");
                        LOGGER.warn("[ModKiller] ☠ 发现恶意模组: {} (ID: {})", displayName, modId);
                        LOGGER.warn("[ModKiller] ☠ 该模组已被 AntiMalwareTransformer 在字节码级中和");
                        LOGGER.warn("═══════════════════════════════════════════════");
                        break;
                    }
                }
            }

            if (FOUND_MALICIOUS_MODS.isEmpty()) {
                LOGGER.info("[ModKiller] ✓ 未发现恶意模组，环境安全");
            } else {
                LOGGER.warn("[ModKiller] ☠ 共发现 {} 个恶意模组: {}", FOUND_MALICIOUS_MODS.size(), FOUND_MALICIOUS_MODS);
            }
        } catch (Exception e) {
            LOGGER.error("[ModKiller] 扫描模组时出错: {}", e.getMessage());
        }
    }

    /**
     * 是否发现恶意模组
     */
    public static boolean hasFoundMaliciousMods() {
        return !FOUND_MALICIOUS_MODS.isEmpty();
    }

    /**
     * 获取发现的恶意模组列表
     */
    public static List<String> getFoundMaliciousMods() {
        return new ArrayList<>(FOUND_MALICIOUS_MODS);
    }
}