package dev.slne.surf.npc.bukkit.api

import com.google.auto.service.AutoService

import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcData
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcLocation
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.skin.BukkitSNpcSkinData
import dev.slne.surf.npc.core.controller.npcController

import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Services

import java.util.UUID

@AutoService(SurfNpcApi::class)
class BukkitSurfNpcApi : SurfNpcApi, Services.Fallback {
    override fun createNpc(
        displayName: Component,
        internalName: String,
        skin: SNpcSkinData,
        location: SNpcLocation,
        global: Boolean,
        rotationType: SNpcRotationType,
        fixedRotation: SNpcRotation?
    ): NpcCreationResult {
        return npcController.createNpc(BukkitSNpcData(
            displayName = displayName,
            internalName = internalName,
            skin = skin,
            location = location,
            global = global,
            rotationType = rotationType,
            fixedRotation = fixedRotation
        ))
    }

    override fun deleteNpc(npc: SNpc): NpcDeletionResult {
        return npcController.deleteNpc(npc)
    }

    override fun showNpc(npc: SNpc, uuid: UUID) {
        npcController.showNpc(npc, uuid)
    }

    override fun hideNpc(npc: SNpc, uuid: UUID) {
        npcController.hideNpc(npc, uuid)
    }

    override fun setSkin(npc: SNpc, skin: SNpcSkinData) {
        npcController.setSkin(npc, skin)
    }

    override fun setRotationType(
        npc: SNpc,
        rotationType: SNpcRotationType
    ) {
        npcController.setRotationType(npc, rotationType)
    }

    override fun setRotation(npc: SNpc, rotation: SNpcRotation) {
        npcController.setRotation(npc, rotation)
    }

    override fun getProperties(npc: SNpc): ObjectSet<SNpcProperty> {
        return npcController.getProperties(npc)
    }

    override fun addProperty(
        npc: SNpc,
        property: SNpcProperty<*>
    ): Boolean {
        return npcController.addProperty(npc, property)
    }

    override fun removeProperty(
        npc: SNpc,
        property: SNpcProperty<*>
    ): Boolean {
        return npcController.removeProperty(npc, property)
    }

    override fun createProperty(
        key: String,
        value: String,
        type: SNpcPropertyType<*>
    ): SNpcProperty<*> {
        return BukkitSNpcProperty(key, value, type)
    }

    override fun getNpc(id: Int): SNpc? {
        return npcController.getNpc(id)
    }

    override fun getNpc(internalName: String): SNpc? {
        return npcController.getNpc(internalName)
    }

    override fun getNpcs(): ObjectList<SNpc> {
        return npcController.getNpcs()
    }

    override fun despawnAllNpcs() {
        npcController.despawnAllNpcs()
    }

    override fun createRotation(
        yaw: Float,
        pitch: Float
    ): SNpcRotation {
        return BukkitSNpcRotation(yaw, pitch)
    }

    override fun createSkinData(
        owner: String,
        value: String,
        signature: String
    ): SNpcSkinData {
        return BukkitSNpcSkinData(owner, value, signature)
    }

    override fun createLocation(
        x: Double,
        y: Double,
        z: Double,
        worldName: String
    ): SNpcLocation {
        return BukkitSNpcLocation(x, y, z, worldName)
    }
}