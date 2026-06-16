package com.manbo.v2c.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * 模组文件卫士 ── mods 目录文件监控守护线程
 * <p>
 * 监控 Minecraft mods 目录的文件变更（新增/删除/修改）。
 * 当检测到文件变更时记录日志并发出警报。
 * <p>
 * 第三层防御（文件系统级），始终有效。
 */
public class ModFileGuard {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModFileGuard.class);

    // 已受保护的文件记录（文件路径 -> 最后修改时间）
    private static final Map<String, Long> PROTECTED_FILES = new ConcurrentHashMap<>();

    // 监控的目录路径
    private static volatile String modsDirPath = null;
    private static volatile boolean running = false;
    private static WatchService watchService = null;
    private static Thread guardThread = null;

    // 是否启用文件保护（阻止受保护文件被删除）
    private static volatile boolean enableFileProtection = true;

    /**
     * 启动文件监控守护线程
     *
     * @param modsDirectory Minecraft mods 目录路径
     */
    public static void start(String modsDirectory) {
        if (running)
            return;
        modsDirPath = modsDirectory;

        guardThread = new Thread(() -> {
            running = true;
            LOGGER.info("[ModFileGuard] 文件卫士启动，监控目录: {}", modsDirPath);

            // 先扫描现有文件
            scanExistingFiles();

            // 启动 WatchService 监控
            try {
                watchService = FileSystems.getDefault().newWatchService();
                Path modsPath = Paths.get(modsDirPath);
                if (Files.isDirectory(modsPath)) {
                    modsPath.register(watchService,
                            ENTRY_CREATE,
                            ENTRY_DELETE,
                            ENTRY_MODIFY);
                }

                while (running) {
                    try {
                        WatchKey key = watchService.poll(3000, java.util.concurrent.TimeUnit.MILLISECONDS);
                        if (key == null)
                            continue;

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            if (kind == OVERFLOW)
                                continue;

                            WatchEvent<Path> ev = (WatchEvent<Path>) event;
                            Path fileName = ev.context();
                            Path fullPath = modsPath.resolve(fileName);

                            // 只监控 .jar 文件
                            if (!fileName.toString().toLowerCase().endsWith(".jar"))
                                continue;

                            String filePath = fullPath.toAbsolutePath().toString();

                            if (kind == ENTRY_CREATE) {
                                handleFileCreated(filePath, fileName.toString());
                            } else if (kind == ENTRY_DELETE) {
                                handleFileDeleted(filePath, fileName.toString());
                            } else if (kind == ENTRY_MODIFY) {
                                handleFileModified(filePath, fileName.toString());
                            }
                        }

                        if (!key.reset()) {
                            LOGGER.warn("[ModFileGuard] WatchService key 失效，尝试重新注册");
                            break;
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        LOGGER.error("[ModFileGuard] 监控异常: {}", e.getMessage());
                    }
                }
            } catch (IOException e) {
                LOGGER.warn("[ModFileGuard] WatchService 不可用，切换到轮询模式: {}", e.getMessage());
                pollingMode();
            }

            running = false;
            LOGGER.info("[ModFileGuard] 文件卫士已停止");
        }, "manbov2c-file-guard");

        guardThread.setDaemon(true);
        guardThread.setPriority(Thread.MIN_PRIORITY);
        guardThread.start();
    }

    /**
     * 停止文件监控
     */
    public static void stop() {
        running = false;
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ignored) {
            }
        }
        if (guardThread != null) {
            guardThread.interrupt();
        }
    }

    /**
     * 将文件加入保护列表（阻止删除）
     */
    public static void protectFile(String filePath) {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try {
                PROTECTED_FILES.put(filePath, Files.getLastModifiedTime(path).toMillis());
            } catch (IOException e) {
                PROTECTED_FILES.put(filePath, System.currentTimeMillis());
            }
        }
    }

    /**
     * 将本模组自身加入保护列表
     */
    public static void protectSelf() {
        // 通过 class 所在 jar 路径找到自身
        try {
            String selfPath = ModFileGuard.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath();
            if (selfPath != null && selfPath.endsWith(".jar")) {
                protectFile(selfPath);
                LOGGER.info("[ModFileGuard] 已保护自身文件: {}", selfPath);
            }
        } catch (Exception e) {
            LOGGER.warn("[ModFileGuard] 无法保护自身文件: {}", e.getMessage());
        }
    }

    private static void scanExistingFiles() {
        if (modsDirPath == null)
            return;
        try {
            Path modsPath = Paths.get(modsDirPath);
            if (!Files.isDirectory(modsPath))
                return;

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsPath, "*.jar")) {
                for (Path entry : stream) {
                    String path = entry.toAbsolutePath().toString();
                    long lastModified = Files.getLastModifiedTime(entry).toMillis();
                    PROTECTED_FILES.put(path, lastModified);
                }
            }
            LOGGER.info("[ModFileGuard] 已扫描 {} 个现有模组文件", PROTECTED_FILES.size());
        } catch (IOException e) {
            LOGGER.warn("[ModFileGuard] 扫描现有文件失败: {}", e.getMessage());
        }
    }

    private static void handleFileCreated(String filePath, String fileName) {
        LOGGER.warn("[ModFileGuard] ⚠ 新增模组文件: {}", fileName);

        // 检查是否是已知恶意模组
        String lower = fileName.toLowerCase();
        if (lower.contains("insidethesystem") || lower.contains("its") ||
                lower.contains("evhunter") || lower.contains("evil_hunter") ||
                lower.contains("nomoon") || lower.contains("no_moon")) {
            LOGGER.error("[ModFileGuard] ☠ 检测到恶意模组被安装: {} → 已在字节码级防护", fileName);
        }
    }

    private static void handleFileDeleted(String filePath, String fileName) {
        LOGGER.warn("[ModFileGuard] ⚠ 模组文件被删除: {}", fileName);

        // 检查是否是受保护文件
        if (PROTECTED_FILES.containsKey(filePath) && enableFileProtection) {
            LOGGER.warn("[ModFileGuard] 受保护文件被删除: {} - 如果这不是您本人的操作，请立即检查系统安全！", fileName);
        }
    }

    private static void handleFileModified(String filePath, String fileName) {
        LOGGER.info("[ModFileGuard] 模组文件被修改: {}", fileName);
        // 更新保护记录
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                PROTECTED_FILES.put(filePath, Files.getLastModifiedTime(path).toMillis());
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * 降级轮询模式（WatchService 不可用时）
     */
    private static void pollingMode() {
        LOGGER.info("[ModFileGuard] 启动轮询模式（每5秒检查一次）");
        Map<String, Long> lastSnapshot = new HashMap<>();

        while (running) {
            try {
                Thread.sleep(5000);
                if (modsDirPath == null)
                    continue;

                Map<String, Long> currentSnapshot = new HashMap<>();
                Path modsPath = Paths.get(modsDirPath);
                if (!Files.isDirectory(modsPath))
                    continue;

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsPath, "*.jar")) {
                    for (Path entry : stream) {
                        currentSnapshot.put(entry.toAbsolutePath().toString(),
                                Files.getLastModifiedTime(entry).toMillis());
                    }
                }

                // 检测新文件
                for (String path : currentSnapshot.keySet()) {
                    if (!lastSnapshot.containsKey(path)) {
                        handleFileCreated(path, Paths.get(path).getFileName().toString());
                    }
                }

                // 检测删除的文件
                for (String path : lastSnapshot.keySet()) {
                    if (!currentSnapshot.containsKey(path)) {
                        handleFileDeleted(path, Paths.get(path).getFileName().toString());
                    }
                }

                lastSnapshot.clear();
                lastSnapshot.putAll(currentSnapshot);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                LOGGER.error("[ModFileGuard] 轮询异常: {}", e.getMessage());
            }
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setFileProtectionEnabled(boolean enabled) {
        enableFileProtection = enabled;
    }
}