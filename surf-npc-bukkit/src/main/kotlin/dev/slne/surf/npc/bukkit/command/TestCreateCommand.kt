package dev.slne.surf.npc.bukkit.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.npc.core.npc.CoreNpc
import dev.slne.surf.surfapi.core.api.util.emptyObjectSet
import dev.slne.surf.surfapi.core.api.util.random
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

class TestCreateCommand(commandName: String): CommandAPICommand(commandName) {
    init {
        stringArgument("name")
        playerExecutor { player, args ->
            val name: String by args
            val entityId = random.nextInt()

            surfNpcApi.spawnNpc(CoreNpc(
                UUID.randomUUID(),
                name,
                Component.text(name, NamedTextColor.RED),
                player.location,
                "",
                emptyObjectSet(),
                entityId
            ))

            player.sendMessage(Component.text("Created NPC with name $name and entity ID $entityId"))
        }
    }
}