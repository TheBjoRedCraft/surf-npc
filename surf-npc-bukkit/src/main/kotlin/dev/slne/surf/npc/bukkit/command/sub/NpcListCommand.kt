package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.bukkit.util.PageableMessageBuilder
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.toPlain
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.format.TextDecoration

class NpcListCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_LIST)
        integerArgument("page", optional = true)
        playerExecutor { player, args ->
            val npcs = npcController.getNpcs()
            val page = args.getOrDefaultUnchecked("page", 1)

            if (npcs.isEmpty()) {
                player.sendText {
                    appendPrefix()
                    error("Es sind keine Npcs vorhanden.")
                }
                return@playerExecutor
            }

            PageableMessageBuilder {
                pageCommand = "/npc list %page%"

                title {
                    info("Npc Informationen".toSmallCaps())
                    decorate(TextDecoration.BOLD)
                }

                npcs.forEach {
                    line {
                        append {
                            info("| ")
                            decorate(TextDecoration.BOLD)
                        }

                        variableValue(it.internalName)
                        spacer(" (ID: ${it.id})")
                    }
                }
            }.send(player, page)
        }
    }
}