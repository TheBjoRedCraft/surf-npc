package dev.slne.surf.npc.bukkit.util

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.bukkit.skin.BukkitSNpcSkinData
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bukkit.Bukkit
import org.bukkit.Location

import java.net.HttpURLConnection
import java.net.URL

suspend fun skinDataFromName(name: String): SNpcSkinData = withContext(Dispatchers.IO) {
    val uuid = PlayerLookupService.getUuid(name) ?: return@withContext skinDataDefault()

    val url = URL("https://api.minetools.eu/profile/$uuid")
    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "GET"
    connection.connectTimeout = 15000
    connection.readTimeout = 15000

    if (connection.responseCode != 200) {
        println("Fehler beim Abrufen der Skin-Daten: ${connection.responseCode} - ${connection.responseMessage}")
        return@withContext skinDataDefault()
    }

    val response = connection.inputStream.bufferedReader().use { it.readText() }

    val json = Json.parseToJsonElement(response).jsonObject
    val properties = json["raw"]?.jsonObject?.get("properties")?.jsonArray ?: return@withContext skinDataDefault()

    val textureProperty = properties.firstOrNull { it.jsonObject["name"]?.jsonPrimitive?.content == "textures" } ?: return@withContext skinDataDefault()
    val textureObj = textureProperty.jsonObject
    val value = textureObj["value"]?.jsonPrimitive?.content ?: return@withContext skinDataDefault()
    val signature = textureObj["signature"]?.jsonPrimitive?.content ?: return@withContext skinDataDefault()

    return@withContext BukkitSNpcSkinData(name, value, signature)
}

fun skinDataDefault() : SNpcSkinData {
    return BukkitSNpcSkinData(
        "default",
        "ewogICJ0aW1lc3RhbXAiIDogMTc0ODc1ODY4OTg4NCwKICAicHJvZmlsZUlkIiA6ICI2NGY0MGFiNzFmM2E0NGZiYjg0N2I5ZWFhOWZjNDRlNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJvZGF2aWRjZXNhciIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNzY5YmJiOWZiMjMxNjgwODEzMWU2YzJkMDJjZTE0ZTNhYWI2NzRkZWI0YjU1OGJiMjY1YzMxNDFkMTk5YjA4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
        "jzdsnHycAav2MCmYQ9jkS8KXgP9Kg3wa7EmC9ALqEDMfDpEHfhyn240HyTr8KZ6P4IhRTtU9NVfdwKfPDTdtt7mx+piIL0VV8R3BFsOrbKC0erkULTZ99b68xZ8viEdlekrN8wIqIBd6Y7W38YKNNTEbtLkZQahCYAssz9DVNxMKnCwdXs+hTl5vPa4DbKiNnUsHR+g2d6S995NRtCiXMsNGfk4BqOqQ6P7bFZUBCjDB6lQREh5SF2lafbPpGhfhl/PuhBSK/5dtNPqyiI5LAqlFJTaUQ6yTKnoN43LtcbrRRKIv00OOznGLCijbv2KBtHvI1qgubRAKyNLZSgvumjl+iAcxhI+rAvQkVAguBgQhLOhEIMcoa4IMo+yW6RY4fIWDb5Rwb3WjVVlA5MWPgZ0apTuc0KSaJx8SkEOqbTPOdzrYo6UdLih4wTXxm0lq+cN7ibmc0ZUkZ8mrLSjF91IVueH15oKooNYZx7NLyExCLVL3R1ZS1eLO2LxqyahpqhhfRpE2GpD3Wx/rNev++03EZ9LBUCmdJ5KYa8RNGHfKrTrl0Fqt4YsDMVXpZjXSR1S/c5KREaHZnkQAsL4dd+I+BD3lTu0RYCgsT/mbDUSWPcLzSnh+jTqdIpsueQ88p5m0MecrtA0J3mEcblyj6PIE2QSAJ6wFv+uDHcVLD8A="
    )
}

fun SNpcLocation.toLocation(): Location? {
    return Location(
        Bukkit.getWorld(world) ?: return null,
        this.x,
        this.y,
        this.z
    )
}

fun SNpcLocation.readableString(): String {
    return "${x.toInt()}, ${y.toInt()}, ${z.toInt()} in '$world'"
}

fun Location.readableString(): String {
    return "${x.toInt()}, ${y.toInt()}, ${z.toInt()} in '$world'"
}

fun SNpc.hideAll() {
    if(this.data.global) {
        forEachPlayer {
            this.despawn(it.uniqueId)
        }
    } else {
        this.viewers.forEach { viewer ->
            this.despawn(viewer)
        }
    }
}

fun SNpc.showAll() {
    if(this.data.global) {
        forEachPlayer {
            this.spawn(it.uniqueId)
        }
    } else {
        this.viewers.forEach { viewer ->
            this.spawn(viewer)
        }
    }
}
