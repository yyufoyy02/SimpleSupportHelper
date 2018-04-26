package com.vk.helper.core.helper.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

class BooleanSerializer : JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

    override fun serialize(arg0: Boolean, arg1: Type,
                           arg2: JsonSerializationContext): JsonElement {
        return JsonPrimitive(if (arg0) 1 else 0)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(arg0: JsonElement, arg1: Type,
                             arg2: JsonDeserializationContext): Boolean {
        return when {
            arg0.asJsonPrimitive.isBoolean -> arg0.asBoolean
            arg0.asJsonPrimitive.isNumber -> arg0.asInt == 1
            arg0.asJsonPrimitive.isString -> "1" == arg0.asString
            else -> false
        }
    }
}
