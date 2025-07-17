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

class NpcPropertyAddCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_PROPERTY_ADD)
        npcArgument("npc")
        stringArgument("key")
        stringArgument("value")
        npcPropertyTypeArgument("propertyType")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val key: String by args
            val value: String by args
            val propertyType: SNpcPropertyType by args

            val exists = npc.hasProperty(key)

            npc.addProperty(BukkitSNpcProperty(
                key, propertyType.decode(value), propertyType
            ))

            player.sendText {
                appendPrefix()

                if(exists) {
                    success("Die Property '${key}' wurde erfolgreich dem NPC '${npc.internalName}' neu gesetzt.")
                } else {
                    success("Die Property '${key}' wurde erfolgreich zum NPC '${npc.internalName}' hinzugefügt.")
                }
            }
        }
    }
}