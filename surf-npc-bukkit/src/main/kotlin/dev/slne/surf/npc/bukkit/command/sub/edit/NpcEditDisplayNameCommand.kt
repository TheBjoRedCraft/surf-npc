package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.miniMessage
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditDisplayNameCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_DISPLAYNAME)
        npcArgument("npc")
        textArgument("displayName")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val displayName: String by args

            val name = miniMessage.deserialize(displayName)

            npc.addProperty(BukkitSNpcProperty(
                SNpcProperty.Internal.DISPLAYNAME,
                name,
                propertyTypeRegistry.get(SNpcPropertyType.Types.COMPONENT) ?: return@playerExecutor
            ))

            npc.refresh()

            player.sendText {
                appendPrefix()
                success("Der Anzeigename des Npc ")
                variableValue(npc.internalName)
                success(" wurden aktualisiert.")
            }
        }
    }
}