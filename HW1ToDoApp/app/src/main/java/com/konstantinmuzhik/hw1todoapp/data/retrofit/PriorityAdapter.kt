package com.konstantinmuzhik.hw1todoapp.data.retrofit

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import java.lang.reflect.Type
import java.util.Locale

class PriorityAdapter : JsonSerializer<Priority>, JsonDeserializer<Priority> {
    override fun serialize(
        src: Priority?,
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
    ): Priority {
        val priorityValue = json?.asString?.uppercase(Locale.ROOT)
        return priorityValue?.let { Priority.valueOf(it) } ?: Priority.NO
    }
}