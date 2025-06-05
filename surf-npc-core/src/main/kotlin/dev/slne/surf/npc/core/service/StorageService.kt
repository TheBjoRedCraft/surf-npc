package dev.slne.surf.npc.core.service

import dev.slne.surf.surfapi.core.api.util.requiredService
import org.jetbrains.annotations.Blocking
import java.nio.file.Path

interface StorageService {
    fun initialize()

    fun loadNpcs()
    fun saveNpcs()

    companion object {
        /**
         * The instance of the StorageService.
         */
        val INSTANCE = requiredService<StorageService>()
    }
}

/**
 * The instance of the DatabaseService
 */
val storageService get() = StorageService.INSTANCE