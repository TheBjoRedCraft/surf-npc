package dev.slne.surf.npc.api.npc

interface SNpcPropertyType {
    val id: String
    fun encode(value: Any): String
    fun decode(value: String): Any


    object Types {
        const val BOOLEAN = "boolean"
        const val INT = "int"
        const val LONG = "long"
        const val STRING = "string"
        const val DOUBLE = "double"
        const val FLOAT = "float"
        const val UUID = "uuid"
        const val LOCATION = "location"
        const val COMPONENT = "component"
        const val NAMED_TEXT_COLOR = "named_text_color"

        const val NPC_ROTATION = "npc_rotation"
    }
}

