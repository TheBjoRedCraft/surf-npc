package dev.slne.surf.npc.bukkit.command.sub.edit

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.hideAll
import dev.slne.surf.npc.bukkit.util.miniMessage
import dev.slne.surf.npc.bukkit.util.showAll
import dev.slne.surf.npc.bukkit.util.skinDataFromName
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditDisplayNameCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT_SKIN)
        npcArgument("npc")
        textArgument("displayName")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val displayName: String by args

            val name = miniMessage.deserialize(displayName)

            npc.hideAll()

            npcController.unregisterNpc(npc)
            npc.data.displayName = name
            npcController.registerNpc(npc)

            npc.showAll()

            player.sendText {
                appendPrefix()
                success("Der Anzeigename des Npc ")
                variableValue(npc.data.internalName)
                success(" wurden aktualisiert.")
            }
        }
    }
}