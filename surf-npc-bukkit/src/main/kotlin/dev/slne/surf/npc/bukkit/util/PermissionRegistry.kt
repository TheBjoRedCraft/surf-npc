package dev.slne.surf.npc.bukkit.util

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {
    const val PERMISSION_PREFIX = "surf.npc"

    const val COMMAND_NPC = "$PERMISSION_PREFIX.command"
    const val COMMAND_NPC_CREATE = "$PERMISSION_PREFIX.command.create"
    const val COMMAND_NPC_DELETE = "$PERMISSION_PREFIX.command.delete"
    const val COMMAND_NPC_REFRESH = "$PERMISSION_PREFIX.command.refresh"

    const val COMMAND_NPC_LIST = "$PERMISSION_PREFIX.command.list"
    const val COMMAND_NPC_INFO = "$PERMISSION_PREFIX.command.info"

    const val COMMAND_NPC_EDIT = "$PERMISSION_PREFIX.command.edit"
    const val COMMAND_NPC_EDIT_ROTATION = "$PERMISSION_PREFIX.command.edit.rotation"
    const val COMMAND_NPC_EDIT_SKIN = "$PERMISSION_PREFIX.command.edit.skin"
    const val COMMAND_NPC_DISPLAYNAME = "$PERMISSION_PREFIX.command.edit.displayname"

    const val COMMAND_NPC_TELEPORT_TO = "$PERMISSION_PREFIX.command.teleport"
    const val COMMAND_NPC_TELEPORT_HERE = "$PERMISSION_PREFIX.command.teleporthere"

    const val COMMAND_NPC_PROPERTY = "$PERMISSION_PREFIX.command.property"
    const val COMMAND_NPC_PROPERTY_ADD = "$PERMISSION_PREFIX.command.property.add"
    const val COMMAND_NPC_PROPERTY_REMOVE = "$PERMISSION_PREFIX.command.property.remove"
    const val COMMAND_NPC_PROPERTY_LIST = "$PERMISSION_PREFIX.command.property.list"

    const val COMMAND_NPC_IMPORT = "$PERMISSION_PREFIX.command.import"
    const val COMMAND_NPC_IMPORT_ALL = "$PERMISSION_PREFIX.command.importall"
    const val COMMAND_NPC_EXPORT = "$PERMISSION_PREFIX.command.export"
    const val COMMAND_NPC_EXPORT_ALL = "$PERMISSION_PREFIX.command.exportall"
    const val COMMAND_NPC_RELOAD_FROM_DISK = "$PERMISSION_PREFIX.command.loadFromDisk"
    const val COMMAND_NPC_SAVE_TO_DISK = "$PERMISSION_PREFIX.command.saveToDisk"
}
