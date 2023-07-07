package com.konstantinmuzhik.hw1todoapp.data.datasource.remote.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import java.lang.reflect.Type
import java.util.Locale

/**
 * Json Serializer and Deserializer for Priority
 *
 * @author Konstantin Kovalev
 *
 */
class PriorityAdapter : JsonSerializer<Priority>, JsonDeserializer<Priority> {
    override fun serialize(
        src: Priority?, typeOfSrc: Type?, context: JsonSerializationContext?,
    ): JsonElement =
        JsonPrimitive(src?.toString().toString().lowercase())

    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?,
    ): Priority =
        json?.asString?.uppercase(Locale.ROOT)?.let { Priority.valueOf(it) } ?: Priority.NO
}