/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.GameType
 *  net.minecraftforge.event.CommandEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$PlayerChangeGameModeEvent
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.server;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(modid = "manbov2c", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandAndGamemodeProtector {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandAndGamemodeProtector.class);
    private static final Map<UUID, GameType> PLAYER_GAME_MODES = new ConcurrentHashMap<UUID, GameType>();
    private static final GameType DEFAULT_GAME_MODE = GameType.CREATIVE;
    private static volatile boolean commandGuardActive = false;

    /**
     * 激活指令守护（由 DefenseController 调用）
     */
    public static void activateCommandGuard() {
        commandGuardActive = true;
        LOGGER.info("[指令守护] ✓ 指令强制放行保护已激活（玩家指令不可被任何模组禁止）");
    }

    public static boolean isCommandGuardActive() {
        return commandGuardActive;
    }

    /**
     * 游戏模式变更保护 — 防止其他模组强制修改玩家游戏模式
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID uuid = player.getUUID();
        GameType newMode = event.getNewGameMode();
        PLAYER_GAME_MODES.put(uuid, newMode);
        if (newMode != GameType.CREATIVE && newMode != GameType.SPECTATOR) {
            LOGGER.warn(
                    "[\u66fc\u6ce2\u00b7\u6307\u4ee4\u4fdd\u62a4] \u73a9\u5bb6 {} \u88ab\u5c1d\u8bd5\u5207\u6362\u81f3 {}\uff0c\u9632\u62a4\u7cfb\u7edf\u5c06\u6062\u590d",
                    (Object) player.getName().getString(), (Object) newMode);
        }
    }

    /**
     * 指令强制执行保护（LOWEST 优先级）
     * <p>
     * 作为 Mixin 的备份：即使有模组在 HIGHEST 取消了 CommandEvent，
     * 本处理器会在 LOWEST 重新放行玩家指令。
     * 配合 {@link com.manbo.v2c.mixin.CommandGuardMixin} 构成双重保险。
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCommandForceExecute(CommandEvent event) {
        if (!commandGuardActive)
            return;

        if (event.isCanceled()) {
            CommandSourceStack source = event.getParseResults().getContext().getSource();
            Entity entity = source.getEntity();
            if (entity instanceof ServerPlayer) {
                // 取消放行 → 玩家指令不能被阻止
                LOGGER.warn("[指令守护] ⚠ 事件级拦截：玩家 {} 的指令被取消，已强制放行",
                        entity.getName().getString());
                event.setCanceled(false);
            }
        }
    }

    public static boolean isPlayerProtected(ServerPlayer player) {
        return player != null && PLAYER_GAME_MODES.containsKey(player.getUUID());
    }

    public static void protectPlayer(ServerPlayer player) {
        if (player == null) {
            return;
        }
        UUID uuid = player.getUUID();
        GameType targetMode = PLAYER_GAME_MODES.getOrDefault(uuid, DEFAULT_GAME_MODE);
        GameType currentMode = player.gameMode.getGameModeForPlayer();
        if (currentMode != targetMode) {
            try {
                player.setGameMode(targetMode);
                LOGGER.info(
                        "[\u66fc\u6ce2\u00b7\u6307\u4ee4\u4fdd\u62a4] \u5df2\u6062\u590d\u73a9\u5bb6 {} \u7684\u6e38\u620f\u6a21\u5f0f\u81f3 {}",
                        (Object) player.getName().getString(), (Object) targetMode);
            } catch (Exception e) {
                LOGGER.error(
                        "[\u66fc\u6ce2\u00b7\u6307\u4ee4\u4fdd\u62a4] \u6062\u590d\u6e38\u620f\u6a21\u5f0f\u5931\u8d25: {}",
                        (Object) e.getMessage());
            }
        }
        if (targetMode == GameType.CREATIVE || targetMode == GameType.SPECTATOR) {
            if (targetMode == GameType.CREATIVE) {
                player.getAbilities().instabuild = true;
            }
            if (targetMode == GameType.CREATIVE || targetMode == GameType.SPECTATOR) {
                player.getAbilities().mayfly = true;
            }
            player.onUpdateAbilities();
        }
    }

    private static void startDaemonWatchdog() {
        Thread watchdog = new Thread(() -> {
            block5: while (true) {
                try {
                    while (true) {
                        Thread.sleep(1000L);
                        Object hookHolder = null;
                        try {
                            Method getPlayerList;
                            Object playerList;
                            Method getPlayers;
                            List players;
                            Class<?> hooksClass = Class.forName("net.minecraftforge.server.ServerLifecycleHooks");
                            Method getServer = hooksClass.getDeclaredMethod("getCurrentServer", new Class[0]);
                            Object server = getServer.invoke(null, new Object[0]);
                            if (server == null || (players = (List) (getPlayers = (playerList = (getPlayerList = server
                                    .getClass().getMethod("getPlayerList", new Class[0])).invoke(server, new Object[0]))
                                    .getClass().getMethod("getPlayers", new Class[0]))
                                    .invoke(playerList, new Object[0])) == null)
                                continue block5;
                            Iterator iterator = players.iterator();
                            while (true) {
                                if (!iterator.hasNext())
                                    continue block5;
                                ServerPlayer player = (ServerPlayer) iterator.next();
                                CommandAndGamemodeProtector.protectPlayer(player);
                            }
                        } catch (Exception exception) {
                            continue;
                        }
                    }
                } catch (InterruptedException e) {
                } catch (Exception exception) {
                    continue;
                }
            }
        }, "manbov2c-gamemode-watchdog");
        watchdog.setDaemon(true);
        watchdog.setPriority(1);
        watchdog.start();
    }

    static {
        CommandAndGamemodeProtector.startDaemonWatchdog();
        LOGGER.info("[\u66fc\u6ce2\u00b7\u6307\u4ee4\u4fdd\u62a4] \u5b88\u62a4\u7ebf\u7a0b\u5df2\u542f\u52a8");
    }
}
