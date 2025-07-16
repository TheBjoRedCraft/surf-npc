package dev.slne.surf.npc.bukkit.command.sub.edit

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class NpcEditRotationCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_EDIT_ROTATION)
        npcArgument("npc")
        rotationTypeArgument("rotationType")
        playerExecutor { player, args ->
            val npc: SNpc by args
            val rotationType: SNpcRotationType by args

            npc.addProperty(BukkitSNpcProperty(
                SNpcProperty.Internal.ROTATION_TYPE,
                rotationType == SNpcRotationType.PER_PLAYER,
                propertyTypeRegistry.get(SNpcPropertyType.Types.BOOLEAN) ?: return@playerExecutor
            ))

            if(rotationType == SNpcRotationType.FIXED) {
                npc.addProperty(BukkitSNpcProperty(
                    SNpcProperty.Internal.ROTATION_FIXED,
                    BukkitSNpcRotation(
                        player.yaw, player.pitch
                    ),
                    propertyTypeRegistry.get(SNpcPropertyType.Types.NPC_ROTATION) ?: return@playerExecutor
                ))
            }

            npc.refresh()

            player.sendText {
                appendPrefix()
                success("Die Rotation des Npc ")
                variableValue(npc.internalName)
                success(" wurde auf $rotationType geändert.")
            }
        }
    }
}