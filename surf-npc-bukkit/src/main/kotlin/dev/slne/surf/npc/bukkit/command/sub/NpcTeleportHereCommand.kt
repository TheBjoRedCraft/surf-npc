package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player

class NpcTeleportHereCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_TELEPORT_TO)
        npcArgument("npc")
        playerArgument("target", true)
        playerExecutor { player, args ->
            val npc: SNpc by args
            val target: Player? by args
            if (target != null) {
                val targetPlayer = target ?: return@playerExecutor run {
                    player.sendText {
                        appendPrefix()
                        error("Der angegebene Spieler ist nicht mehr online.")
                    }
                }

                npc.teleport(targetPlayer)
                player.sendText {
                    appendPrefix()
                    success("Der NPC ${npc.internalName} wurde zu ${targetPlayer.name} teleportiert.")
                }
                return@playerExecutor
            }

            npc.teleport(player)
            player.sendText {
                appendPrefix()
                success("Der NPC ${npc.internalName} wurde zu dir teleportiert.")
            }
            return@playerExecutor
        }
    }
}