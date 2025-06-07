package dev.slne.surf.npc.bukkit.util

object PermissionRegistry {
    const val PERMISSION_PREFIX = "surf.npc"

    const val COMMAND_NPC = "$PERMISSION_PREFIX.command"
    const val COMMAND_NPC_CREATE = "$PERMISSION_PREFIX.command.create"
    const val COMMAND_NPC_DELETE = "$PERMISSION_PREFIX.command.delete"

    const val COMMAND_NPC_LIST = "$PERMISSION_PREFIX.command.list"
    const val COMMAND_NPC_INFO = "$PERMISSION_PREFIX.command.info"

    const val COMMAND_NPC_EDIT = "$PERMISSION_PREFIX.command.edit"
    const val COMMAND_NPC_EDIT_ROTATION = "$PERMISSION_PREFIX.command.edit.rotation"
    const val COMMAND_NPC_EDIT_SKIN = "$PERMISSION_PREFIX.command.edit.skin"
    const val COMMAND_NPC_DISPLAYNAME = "$PERMISSION_PREFIX.command.edit.displayname"

    const val COMMAND_NPC_TELEPORT_TO = "$PERMISSION_PREFIX.command.teleport"
    const val COMMAND_NPC_TELEPORT_HERE = "$PERMISSION_PREFIX.command.teleporthere"
}