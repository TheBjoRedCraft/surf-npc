package dev.slne.surf.npc.core.service

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.nio.file.Path

interface DatabaseService {
    fun connect(path: Path)

    fun loadNpcs()
    fun saveNpcs()

    companion object {
        /**
         * The instance of the NpcController.
         */
        val INSTANCE = requiredService<DatabaseService>()
    }
}

/**
 * The instance of the DatabaseService
 */
val databaseService get() = DatabaseService.INSTANCE