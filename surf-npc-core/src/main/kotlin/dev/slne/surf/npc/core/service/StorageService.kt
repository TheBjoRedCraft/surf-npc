package dev.slne.surf.npc.core.service

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.surfapi.core.api.util.requiredService
import org.jetbrains.annotations.Blocking
import java.nio.file.Path

interface StorageService {
    fun initialize()

    fun loadNpcs(): Int
    fun saveNpcs(): Int

    fun import(fileName: String): Boolean
    fun export(npc: Npc)

    fun importAll(): Int
    fun exportAll(): Int
    fun reloadFromDisk(): Int
    fun saveToDisk(): Int

    companion object {
        /**
         * The instance of the StorageService.
         */
        val INSTANCE = requiredService<StorageService>()
    }
}

/**
 * The instance of the StorageService
 */
val storageService get() = StorageService.INSTANCE