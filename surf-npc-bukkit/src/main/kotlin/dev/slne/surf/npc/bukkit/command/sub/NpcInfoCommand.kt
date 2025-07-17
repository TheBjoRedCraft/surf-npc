package dev.slne.surf.npc.bukkit.command.sub

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.bukkit.command.argument.npcArgument
import dev.slne.surf.npc.bukkit.util.PermissionRegistry
import dev.slne.surf.npc.bukkit.util.readableString
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.clickRunsCommand
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location

class NpcInfoCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC_INFO)
        npcArgument("npc")
        playerExecutor { player, args ->
            val npc: SNpc by args

            val displayName = npc.getProperty(SNpcProperty.Internal.DISPLAYNAME)?.value as? Component ?: error("NPC ${npc.internalName} has no display name set")
            val location = npc.getProperty(SNpcProperty.Internal.LOCATION)?.value as? SNpcLocation ?: error("NPC ${npc.internalName} has no location set")

            val rotationType = npc.getProperty(SNpcProperty.Internal.ROTATION_TYPE)?.value as? Boolean ?: error("NPC ${npc.internalName} has no rotation type set")
            val skinOwner = npc.getProperty(SNpcProperty.Internal.SKIN_OWNER)?.value as? String ?: error("NPC ${npc.internalName} has no skin owner set")
            val global = npc.getProperty(SNpcProperty.Internal.VISIBILITY_GLOBAL)?.value as? Boolean ?: error("NPC ${npc.internalName} has no global visibility set")

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
                variableValue(npc.internalName)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Anzeigename: ")
                append(displayName)
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
                variableKey("Nametag-ID: ")
                variableValue(npc.nameTagId)
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
                    variableValue(location.readableString())
                    clickRunsCommand("/npc teleport ${npc.id}")
                }
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Rotation: ")
                variableValue(if (rotationType) "Per-Player" else "Fixed")
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Skin: ")
                variableValue(skinOwner)
                appendNewline()

                append {
                    info("| ")
                    decorate(TextDecoration.BOLD)
                }
                variableKey("Global: ")
                variableValue(if (global) "Ja" else "Nein")
            }
        }
    }
}