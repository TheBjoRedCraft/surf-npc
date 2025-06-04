package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.readableString
import dev.slne.surf.npc.bukkit.util.toPlain
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.clickRunsCommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.format.TextDecoration

class NpcInfoCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_INFO)
        npcArgument("npc")
        playerExecutor { player, args ->
            val npc: SNpc by args

            player.sendText {
                append {
                    info("Npc Informationen".toSmallCaps())
                    decorate(TextDecoration.BOLD)
                }
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Name: ")
                variableValue(npc.data.name.toPlain())
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Anzeigename: ")
                append(npc.data.name)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("ID: ")
                variableValue(npc.id)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Uuid: ")
                variableValue(npc.npcUuid.toString())
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                append {
                    variableKey("Ort: ")
                    variableValue(npc.data.location.readableString())
                    clickRunsCommand("/npc teleport ${npc.id}")
                }
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Rotation: ")
                variableValue(npc.data.rotationType.name)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Global: ")
                variableValue(if (npc.data.global) "Ja" else "Nein")
            }
        }
    }
}