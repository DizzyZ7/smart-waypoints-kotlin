package com.dizzyz7.waypoints

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

class WaypointsMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("smart-waypoints")

    override fun onInitialize() {
        logger.info("Smart Waypoints (Kotlin) initialized!")

        // 1. Отслеживание смерти игрока
        ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, _ ->
            val pos = oldPlayer.blockPos
            val message = "☠ Последняя смерть: [${pos.x}, ${pos.y}, ${pos.z}]"
            
            // Отправляем сообщение в чат и логируем
            newPlayer.sendMessage(Text.literal("§c$message"), false)
        }

        // 2. Команда /waypoint set (Современный Command API)
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                literal("waypoint").then(
                    literal("set").executes { context ->
                        val player = context.source.player ?: return@executes 0
                        val pos = player.blockPos
                        
                        context.source.sendFeedback({ 
                            Text.literal("§b📍 Метка установлена: §f${pos.x}, ${pos.y}, ${pos.z}") 
                        }, false)
                        1
                    }
                )
            )
        }
    }
}
