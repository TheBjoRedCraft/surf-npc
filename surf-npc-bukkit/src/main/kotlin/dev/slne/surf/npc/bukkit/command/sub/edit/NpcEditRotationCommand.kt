package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.hideAll
import dev.slne.surf.npc.bukkit.util.showAll
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditRotationCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT_ROTATION)
        npcArgument("npc")
        rotationTypeArgument("rotationType")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val rotationType: SNpcRotationType by args

            npc.hideAll()
            npcController.unregisterNpc(npc)

            npc.data.rotationType = rotationType
            npc.data.fixedRotation = BukkitSNpcRotation(
                player.yaw,
                player.pitch
            )

            npcController.registerNpc(npc)
            npc.showAll()

            player.sendText {
                appendPrefix()
                success("Die Rotation des Npc ")
                variableValue(npc.data.internalName)
                success(" wurde auf $rotationType geändert.")
            }
        }
    }
}