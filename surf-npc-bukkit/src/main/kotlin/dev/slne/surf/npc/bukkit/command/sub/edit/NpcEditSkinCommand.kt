package dev.slne.surf.npc.bukkit.command.sub.edit

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.hideAll
import dev.slne.surf.npc.bukkit.util.showAll
import dev.slne.surf.npc.bukkit.util.skinDataFromName
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditSkinCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT_SKIN)
        npcArgument("npc")
        stringArgument("skinPlayer")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val skinPlayer: String by args

            player.sendText {
                appendPrefix()
                info("Die Skin-Daten für den Spieler ")
                variableValue(skinPlayer)
                info(" werden geladen...")
            }

            plugin.launch {
                val skinData = skinDataFromName(skinPlayer)

                npc.hideAll()
                npcController.unregisterNpc(npc)

                npc.data.skin = skinData

                npcController.registerNpc(npc)
                npc.showAll()

                player.sendText {
                    appendPrefix()
                    success("Die Skin-Daten für den Npc ")
                    variableValue(npc.data.internalName)
                    success(" wurden aktualisiert.")
                }
            }
        }
    }
}