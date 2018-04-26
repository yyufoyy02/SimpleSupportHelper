package com.vk.helper.core.helper.gson

/**
 * Created by youhouchang on 2017/3/28.
 */

import android.util.Log

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

/**
 * Json对象与String的Gson序列化反序列号工具类
 *
 */
class StringJsonDeSerializer<T> : JsonSerializer<T>, JsonDeserializer<T> {

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(Gson().toJson(src))
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T? {
        val jsonString: String = try {
            json.asString
        } catch (e: Exception) {
            json.toString()
        }

        return try {
            GsonBuilder().create().fromJson<T>(jsonString, typeOfT)
        } catch (ex: Exception) {
            Log.e(TAG, String.format("Json deserialize failed, type=<%s>, json=\"%s\"", typeOfT.javaClass.simpleName, jsonString), ex)
            null
        }
    }

    companion object {
        private val TAG = String.format("StringJsonDeSerializer")
    }
}
