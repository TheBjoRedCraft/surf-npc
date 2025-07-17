package dev.slne.surf.npc.bukkit.command.sub.property

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.npcPropertyTypeArgument
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.toPlain
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcPropertyRemoveCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY_REMOVE)
        npcArgument("npc")
        stringArgument("key")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val key: String by args

            if(!npc.hasProperty(key)) {
                player.sendText {
                    appendPrefix()
                    error("Der NPC '${npc.internalName}' besitzt keine Property mit dem Key '${key}'.")
                }
                return@playerExecutor
            }

            npc.removeProperty(key)

            player.sendText {
                appendPrefix()
                success("Die Property '${key}' wurde erfolgreich vom NPC '${npc.internalName}' entfernt.")
            }
        }
    }
}