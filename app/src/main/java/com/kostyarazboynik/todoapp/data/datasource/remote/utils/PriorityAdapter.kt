package com.kostyarazboynik.todoapp.data.datasource.remote.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kostyarazboynik.todoapp.data.models.Importance
import java.lang.reflect.Type
import java.util.Locale

/**
 * Json Serializer and Deserializer for Priority
 *
 * @author Konstantin Kovalev
 *
 */
class PriorityAdapter : JsonSerializer<Importance>, JsonDeserializer<Importance> {
    override fun serialize(
        src: Importance?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {

        val enumValue = src?.toString().toString().lowercase()
        return JsonPrimitive(enumValue)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Importance {
        val importanceValue = json?.asString?.uppercase(Locale.ROOT)
        return importanceValue?.let { Importance.valueOf(it) } ?: Importance.NO
    }
}