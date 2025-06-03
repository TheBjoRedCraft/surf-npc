package dev.slne.surf.npc.bukkit.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.command.sub.NpcCreateCommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

class NpcCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC)
        subcommand(NpcCreateCommand("create"))
    }
}