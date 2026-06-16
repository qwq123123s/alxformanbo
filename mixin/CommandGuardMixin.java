package com.manbo.v2c.mixin;

import com.manbo.v2c.server.DefenseController;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 指令守护 Mixin — 玩家指令不可被任何方式禁止
 * <p>
 * 在字节码层重定向 Forge 的 CommandEvent 取消检查，
 * 确保玩家发送的指令无视其他模组的取消操作，始终执行。
 * 属于诗宝之爱防御体系的指令层终极防护。
 */
@Mixin(Commands.class)
public abstract class CommandGuardMixin {

    private static final Logger LOGGER = LoggerFactory.getLogger("CommandGuard");

    /**
     * 拦截 {@code performCommand} 中的 {@code IEventBus.post(Event)} 调用
     * <p>
     * Forge 在 {@code Commands.performCommand} 中通过 {@code CommandEvent} 让
     * 其他模组有机会取消指令。本重定向依然触发事件（保证兼容性），
     * 但当玩家指令被取消时，强制返回 {@code false}（未取消），
     * 使指令正常执行。
     */
    @Redirect(method = "performCommand", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/eventbus/IEventBus;post(Lnet/minecraftforge/eventbus/Event;)Z"), remap = false)
    private boolean manbov2c_redirectCommandEvent(IEventBus bus, Event event) {
        // 先正常触发事件（让其他模组能监听到指令，但不允许取消）
        boolean cancelled = bus.post(event);

        if (cancelled && event instanceof CommandEvent cmdEvent) {
            CommandSourceStack source = cmdEvent.getParseResults().getContext().getSource();
            Entity entity = source.getEntity();
            if (entity instanceof ServerPlayer) {
                // 玩家指令被恶意取消 → 强制放行
                LOGGER.warn("[指令守护] ⚠ 检测到玩家 {} 的指令被取消，已强制放行",
                        entity.getName().getString());
                return false;
            }
        }

        return cancelled;
    }
}