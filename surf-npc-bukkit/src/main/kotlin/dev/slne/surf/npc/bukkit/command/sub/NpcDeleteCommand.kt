package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.toPlain
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcDeleteCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_DELETE)
        npcArgument("npc")
        playerExecutor { player, args ->
            val npc: SNpc by args

            npc.delete()

            if(npcController.getNpc(npc.id) == null) {
                player.sendText {
                    appendPrefix()
                    success("Der Npc ")
                    variableValue(npc.internalName)
                    success(" wurde gelöscht.")
                }
            } else {
                player.sendText {
                    appendPrefix()
                    error("Der Npc ${npc.internalName} konnte nicht gelöscht werden.")
                }
            }
        }
    }
}