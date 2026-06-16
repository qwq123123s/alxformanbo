package com.manbo.v2c.agent;

import com.manbo.v2c.server.DefenseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * manbov2c Agent 加载器
 * 360°模式：尝试通过 Attach API 动态加载 Java Agent
 * 回退方案：提示用户添加 -javaagent JVM 参数
 */
public class AgentLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger("AgentLoader");
    private static final String AGENT_JAR_NAME = "manbov2c-agent.jar";
    private static volatile boolean loaded = false;

    /**
     * 尝试动态加载 Java Agent（360° 模式专用）
     */
    public static boolean tryLoadAgent() {
        if (loaded) {
            LOGGER.info("[AgentLoader] Agent 已经加载");
            return true;
        }

        LOGGER.info("[AgentLoader] 正在尝试加载 manbov2c Agent (360° 模式)...");

        // 尝试方法1: Attach API 动态加载
        if (tryAttachApi()) {
            loaded = true;
            LOGGER.info("[AgentLoader] ✅ Agent 通过 Attach API 动态加载成功");
            return true;
        }

        // 尝试方法2: 检查是否已通过 -javaagent 启动加载
        if (checkPreloadedAgent()) {
            loaded = true;
            LOGGER.info("[AgentLoader] ✅ Agent 已通过 -javaagent 预先加载");
            return true;
        }

        LOGGER.warn("[AgentLoader] ⚠ Agent 动态加载失败，建议在 JVM 参数中添加:");
        LOGGER.warn("[AgentLoader]   -javaagent:manbov2c-agent.jar");
        LOGGER.warn("[AgentLoader] 当前为标准模式，防护能力有限");
        return false;
    }

    /**
     * 通过 Attach API 动态加载 Agent JAR
     */
    private static boolean tryAttachApi() {
        try {
            // 从主 JAR 中提取 Agent JAR
            File agentJar = extractAgentJar();
            if (agentJar == null || !agentJar.exists()) {
                LOGGER.warn("[AgentLoader] 无法提取 Agent JAR");
                return false;
            }

            // 获取当前 JVM PID
            String pid = getCurrentPid();
            if (pid == null) {
                LOGGER.warn("[AgentLoader] 无法获取当前 JVM PID");
                return false;
            }

            LOGGER.info("[AgentLoader] 当前 JVM PID: {}", pid);

            // 使用反射调用 Attach API（避免编译时依赖 tools.jar）
            Class<?> vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
            java.lang.reflect.Method attachMethod = vmClass.getMethod("attach", String.class);
            Object vm = attachMethod.invoke(null, pid);

            java.lang.reflect.Method loadAgentMethod = vmClass.getMethod("loadAgent", String.class);
            loadAgentMethod.invoke(vm, agentJar.getAbsolutePath());

            java.lang.reflect.Method detachMethod = vmClass.getMethod("detach");
            detachMethod.invoke(vm);

            LOGGER.info("[AgentLoader] Agent 已通过 Attach API 注入 JVM");
            return true;

        } catch (ClassNotFoundException e) {
            LOGGER.warn("[AgentLoader] Attach API 不可用 (jdk.attach 模块未加载)");
            return false;
        } catch (Exception e) {
            LOGGER.warn("[AgentLoader] Attach API 加载失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查是否已通过 -javaagent 启动参数预先加载
     */
    private static boolean checkPreloadedAgent() {
        try {
            // 检查 ManboAgent 是否已经注册
            Class<?> agentClass = Class.forName("com.manbo.v2c.agent.ManboAgent");
            java.lang.reflect.Method isActive = agentClass.getMethod("isActive");
            return (boolean) isActive.invoke(null);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从主 JAR 中提取 Agent JAR 到临时目录
     */
    private static File extractAgentJar() {
        try {
            // 资源路径：Agent JAR 打包在主 JAR 的 resources 中
            String resourcePath = "META-INF/" + AGENT_JAR_NAME;

            // 检查类路径中是否有独立 agent jar
            String classpath = System.getProperty("java.class.path", "");
            for (String entry : classpath.split(File.pathSeparator)) {
                if (entry.contains(AGENT_JAR_NAME)) {
                    File jarFile = new File(entry);
                    if (jarFile.exists()) {
                        return jarFile;
                    }
                }
            }

            // 从主 JAR 提取
            try (InputStream is = AgentLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (is != null) {
                    File tempDir = new File(System.getProperty("java.io.tmpdir"), "manbov2c");
                    tempDir.mkdirs();
                    File tempJar = new File(tempDir, AGENT_JAR_NAME);
                    Files.copy(is, tempJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    tempJar.deleteOnExit();
                    return tempJar;
                }
            }

            return null;
        } catch (Exception e) {
            LOGGER.warn("[AgentLoader] 提取 Agent JAR 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前 JVM 的进程 ID
     */
    private static String getCurrentPid() {
        try {
            String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
            int atIndex = runtimeName.indexOf('@');
            if (atIndex > 0) {
                return runtimeName.substring(0, atIndex);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isLoaded() {
        return loaded;
    }
}