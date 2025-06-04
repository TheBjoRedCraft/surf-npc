package dev.slne.surf.npc.bukkit.command.sub

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.textArgument

import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.command.argument.rotationTypeArgument
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcData
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcLocation
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.skinDataFromName
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

import net.kyori.adventure.text.minimessage.MiniMessage

class NpcCreateCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_CREATE)
        textArgument("name")
        stringArgument("skin")
        rotationTypeArgument("rotationType")
        playerExecutor { player, args ->
            val name: String by args
            val skin: String by args
            val rotationType: SNpcRotationType by args
            val location = player.location

            if(!this.isValidName(name)) {
                player.sendText {
                    appendPrefix()
                    error("Der Npc Name ist ungültig.")
                }
                return@playerExecutor
            }

            player.sendText {
                appendPrefix()
                info("Der Npc wird erstellt. Dies kann einen Moment dauern...")
            }

            val parsedName = MiniMessage.miniMessage().deserialize(name)

            plugin.launch {
                val skinData = skinDataFromName(skin)
                val npcResult = npcController.createNpc(BukkitSNpcData(
                    name = parsedName,
                    skinData,
                    BukkitSNpcLocation(location.x, location.y, location.z, location.world.name),
                    rotationType,
                    BukkitSNpcRotation(location.yaw, location.pitch),
                    true
                    ))

                val npc = npcController.getNpc(name)

                if(npc == null) {
                    player.sendText {
                        appendPrefix()
                        error("Der Npc konnte nicht erstellt werden.")
                    }
                    return@launch
                }

                forEachPlayer {
                    npc.show(it.uniqueId)
                }

                if(npcResult == NpcCreationResult.SUCCESS) {
                    player.sendText {
                        appendPrefix()
                        success("Der Npc wurde erfolgreich erstellt.")
                    }
                } else {
                    player.sendText {
                        appendPrefix()
                        error("Der Npc konnte nicht erstellt werden. ${npcResult.name}")
                    }
                }
            }
        }
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && (name.length > 1 || !name[0].isDigit())
    }
}