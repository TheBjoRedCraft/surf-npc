package dev.slne.surf.npc.bukkit.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.npc.bukkit.command.sub.NpcCreateCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcDeleteCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcInfoCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcListCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcTeleportHereCommand
import dev.slne.surf.npc.bukkit.command.sub.NpcTeleportToCommand
import dev.slne.surf.npc.bukkit.command.sub.edit.NpcEditCommand
import dev.slne.surf.npc.bukkit.util.PermissionRegistry

class NpcCommand(commandName: String) : CommandAPICommand(commandName) {
    init {
        withPermission(PermissionRegistry.COMMAND_NPC)
        subcommand(NpcCreateCommand("create"))
        subcommand(NpcDeleteCommand("delete"))
        subcommand(NpcInfoCommand("info"))
        subcommand(NpcListCommand("list"))
        subcommand(NpcEditCommand("edit"))
        subcommand(NpcTeleportToCommand("teleport"))
        subcommand(NpcTeleportHereCommand("teleporthere"))
    }
}