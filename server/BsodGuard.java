/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.server;

import java.security.Permission;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BsodGuard {
    private static final Logger LOGGER = LoggerFactory.getLogger(BsodGuard.class);
    private static final Set<String> BLOCKED_COMMAND_KEYWORDS = new CopyOnWriteArraySet<String>();
    private static final Set<String> BLOCKED_DLL_KEYWORDS = new CopyOnWriteArraySet<String>();
    private static final Set<String> PROTECTED_SYSTEM_PATHS = new CopyOnWriteArraySet<String>();
    private static final Map<Long, String> SAFE_PROCESSES = new ConcurrentHashMap<Long, String>();
    private static volatile boolean securityManagerInstalled = false;

    private static void initBlockedKeywords() {
        BLOCKED_COMMAND_KEYWORDS.addAll(
                Arrays.asList("shutdown", "taskkill", "taskill", "tskill", "format", "del /f", "rd /s /q", "rmdir /s",
                        "diskpart", "bcdedit", "bootsect", "reg delete", "regedit", "sc stop", "net stop", "net start",
                        "powershell", "wmic", "rundll32", "regsvr32", "cscript", "wscript", "cmd.exe /c", "cmd /c",
                        "notmyfault", "win32k", "int3", "bsod", "crash", "bugcheck", "keBugCheck", "NtRaiseHardError"));
        // 恶意模组特定命令关键词
        BLOCKED_COMMAND_KEYWORDS.addAll(Arrays.asList(
                "insidethesystem", "evilhunter", "ev_hunter", "nomoon", "no_moon",
                "steam", "steam.exe", "mspaint", "calc.exe", "notepad.exe",
                "powershell -windowstyle hidden", "invoke-expression", "invoke-webrequest",
                "start-process -windowstyle hidden", "mshta", "wscript.shell",
                "schtasks", "attrib -r", "vssadmin", "bcdedit /set",
                "wevtutil", "wmic process call create", "reg add",
                "net user", "net localgroup", "takeown", "icacls"));
        BLOCKED_DLL_KEYWORDS.addAll(Arrays.asList("pig2", "kakiku", "omnimobs", "flashfur", "corner", "horror", "scare",
                "inject", "hook", "detour", "crash", "bugcheck"));
        // 恶意模组 DLL/类加载关键词
        BLOCKED_DLL_KEYWORDS.addAll(Arrays.asList(
                "insidethesystem", "its_md", "evhunter", "evil_hunter", "nomoon", "no_moon",
                "mcreator.insidethesystem", "mcreator.evhunter", "mcreator.nomoon"));
        PROTECTED_SYSTEM_PATHS.addAll(Arrays.asList(
                System.getenv("SystemRoot") != null ? System.getenv("SystemRoot").toLowerCase() : "c:\\windows",
                System.getenv("SystemDrive") != null ? System.getenv("SystemDrive").toLowerCase() + "\\" : "c:\\",
                "c:\\windows\\system32", "c:\\windows\\system32\\drivers", "c:\\boot", "c:\\programdata"));
    }

    private static void installSecurityManager() {
        try {
            SecurityManager customSM = new SecurityManager() {

                @Override
                public void checkExec(String cmd) {
                    String lower = cmd.toLowerCase();
                    for (String keyword : BLOCKED_COMMAND_KEYWORDS) {
                        if (!lower.contains(keyword.toLowerCase()))
                            continue;
                        LOGGER.warn("[BSOD\u9632\u62a4] \u2718 \u62e6\u622a\u5371\u9669\u547d\u4ee4: {}", (Object) cmd);
                        throw new SecurityException(
                                "[\u8bd7\u5b9d\u4e4b\u7231] \u62e6\u622a\u5371\u9669\u7cfb\u7edf\u547d\u4ee4: " + cmd);
                    }
                }

                @Override
                public void checkLink(String lib) {
                    String lower = lib.toLowerCase();
                    for (String keyword : BLOCKED_DLL_KEYWORDS) {
                        if (!lower.contains(keyword.toLowerCase()))
                            continue;
                        LOGGER.warn("[BSOD\u9632\u62a4] \u2718 \u62e6\u622a\u53ef\u7591DLL\u52a0\u8f7d: {}",
                                (Object) lib);
                        throw new SecurityException("[\u8bd7\u5b9d\u4e4b\u7231] \u62e6\u622a\u53ef\u7591DLL: " + lib);
                    }
                }

                @Override
                public void checkDelete(String file) {
                    this.checkSystemPath(file, "\u5220\u9664");
                }

                @Override
                public void checkWrite(String file) {
                    this.checkSystemPath(file, "\u5199\u5165");
                }

                @Override
                public void checkRead(String file) {
                }

                private void checkSystemPath(String path, String operation) {
                    if (path == null) {
                        return;
                    }
                    String lower = path.toLowerCase().replace('/', '\\');

                    // 允许临时目录操作（Java NIO / Netty 使用）
                    String tmpDir = System.getProperty("java.io.tmpdir");
                    if (tmpDir != null && lower.startsWith(tmpDir.toLowerCase().replace('/', '\\'))) {
                        return;
                    }
                    String envTemp = System.getenv("TEMP");
                    if (envTemp != null && lower.startsWith(envTemp.toLowerCase().replace('/', '\\'))) {
                        return;
                    }
                    String envTmp = System.getenv("TMP");
                    if (envTmp != null && lower.startsWith(envTmp.toLowerCase().replace('/', '\\'))) {
                        return;
                    }

                    for (String sysPath : PROTECTED_SYSTEM_PATHS) {
                        if (!lower.startsWith(sysPath))
                            continue;
                        if (lower.contains("minecraft") || lower.contains(".minecraft")) {
                            return;
                        }
                        if (lower.contains("manbov2c")) {
                            return;
                        }
                        LOGGER.warn("[BSOD\u9632\u62a4] \u2718 \u62e6\u622a\u7cfb\u7edf\u8def\u5f84{}: {}",
                                (Object) operation, (Object) path);
                        throw new SecurityException("[\u8bd7\u5b9d\u4e4b\u7231] \u62e6\u622a\u7cfb\u7edf\u8def\u5f84"
                                + operation + ": " + path);
                    }
                }

                @Override
                public void checkPermission(Permission perm) {
                }

                @Override
                public void checkPermission(Permission perm, Object context) {
                }
            };
            try {
                System.setSecurityManager(customSM);
                securityManagerInstalled = true;
                LOGGER.info("[BSOD\u9632\u62a4] SecurityManager \u5df2\u5b89\u88c5");
            } catch (Exception e) {
                LOGGER.warn(
                        "[BSOD\u9632\u62a4] SecurityManager \u5b89\u88c5\u5931\u8d25\uff08Java 17+ \u9650\u5236\uff09: {}",
                        (Object) e.getMessage());
                BsodGuard.installFallbackProtection();
            }
        } catch (Exception e) {
            LOGGER.error("[BSOD\u9632\u62a4] \u5b89\u88c5\u5931\u8d25: {}", (Object) e.getMessage());
        }
    }

    private static void installFallbackProtection() {
        LOGGER.info("[BSOD\u9632\u62a4] \u542f\u52a8\u5907\u7528\u9632\u62a4\u65b9\u6848");
    }

    private static void startProcessWatchdog() {
        Thread watchdog = new Thread(() -> {
            block5: while (true) {
                try {
                    while (true) {
                        Thread.sleep(2000L);
                        try {
                            Set childProcesses = ProcessHandle.current().children().collect(Collectors.toSet());
                            Iterator iterator = childProcesses.iterator();
                            block7: while (true) {
                                String keyword;
                                Optional<String> cmdLine;
                                if (!iterator.hasNext())
                                    continue block5;
                                ProcessHandle process = (ProcessHandle) iterator.next();
                                if (!process.isAlive() || !(cmdLine = process.info().commandLine()).isPresent())
                                    continue;
                                String cmd = cmdLine.get().toLowerCase();
                                Iterator<String> iterator2 = BLOCKED_COMMAND_KEYWORDS.iterator();
                                do {
                                    if (!iterator2.hasNext())
                                        continue block7;
                                } while (!cmd.contains((keyword = iterator2.next()).toLowerCase()));
                                LOGGER.warn(
                                        "[BSOD\u9632\u62a4] \u2718 \u53d1\u73b0\u5e76\u7ec8\u6b62\u53ef\u7591\u5b50\u8fdb\u7a0b: {}",
                                        (Object) cmd);
                                process.destroyForcibly();
                            }
                        } catch (Exception childProcesses) {
                            continue;
                        }
                    }
                } catch (InterruptedException e) {
                } catch (Exception exception) {
                    continue;
                }
            }
        }, "manbov2c-bsod-watchdog");
        watchdog.setDaemon(true);
        watchdog.setPriority(1);
        watchdog.start();
    }

    public static boolean isSecurityManagerInstalled() {
        return securityManagerInstalled;
    }

    /**
     * 显式初始化（触发 static 块）
     */
    public static void init() {
        // 静态块已自动执行, 此方法仅用于防御中枢的显式调用
    }

    public static void addBlockedCommand(String command) {
        if (command != null && !command.isEmpty()) {
            BLOCKED_COMMAND_KEYWORDS.add(command.toLowerCase());
        }
    }

    public static void addBlockedDll(String dllName) {
        if (dllName != null && !dllName.isEmpty()) {
            BLOCKED_DLL_KEYWORDS.add(dllName.toLowerCase());
        }
    }

    static {
        BsodGuard.initBlockedKeywords();
        BsodGuard.installSecurityManager();
        BsodGuard.startProcessWatchdog();
        LOGGER.info("[BSOD\u9632\u62a4] \u7cfb\u7edf\u5df2\u521d\u59cb\u5316");
    }
}
