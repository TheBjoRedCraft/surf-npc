package dev.slne.surf.npc.api.npc

interface SNpcProperty {
    val key: String
    val value: Any
    val type: SNpcPropertyType

    object Internal {
        const val DISPLAYNAME = "displayname"

        const val SKIN_OWNER = "skin_owner"
        const val SKIN_TEXTURE = "skin_texture"
        const val SKIN_SIGNATURE = "skin_signature"

        const val LOCATION = "location"
        const val VISIBILITY_GLOBAL = "visibility_global"

        const val ROTATION_TYPE = "rotation_type"
        const val ROTATION_FIXED = "rotation"
    }
}
