package com.vk.helper.core.helper

import com.google.gson.*
import com.vk.helper.core.helper.gson.BooleanSerializer
import com.vk.helper.core.helper.gson.SafeTypeAdapterFactory
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Gson辅助工具类
 *
 * @author Viking
 * @date 2018/4/26
 * @version V1.0.0 < 创建 >
 */
object GsonHelper {

    val gson: Gson by lazy {
        GsonBuilder()
                .registerTypeAdapter(Boolean::class.java, BooleanSerializer())
                .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanSerializer())
                .registerTypeAdapterFactory(SafeTypeAdapterFactory())
                .create()
    }

    var jsonParser = JsonParser()

    @JvmStatic
    fun getJsonElement(s: String): JsonElement {
        return jsonParser.parse(s)
    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    @JvmStatic
    fun objectToJson(ts: Any): String {
        return gson.toJson(ts)
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param <T>
     * @param jsonStr
     * @param type
     * @return
    </T> */

    @JvmStatic
    fun <T> jsonToList(jsonStr: String): T? {
        return gson.fromJson(jsonStr, object : com.google.gson.reflect.TypeToken<List<T>>() {
        }.type)
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    @JvmStatic
    fun jsonToMap(jsonStr: String): Map<*, *>? {
        return gson.fromJson(jsonStr, object : com.google.gson.reflect.TypeToken<Map<*, *>>() {
        }.type)
    }

    /**
     * 将json转换成bean对象
     *
     * @param <T>
     * @param jsonStr
     * @return
    </T> */
    @JvmStatic
    fun <T> jsonToBean(jsonStr: String, cl: Class<T>): T? {
        return try {
            gson.fromJson(jsonStr, cl)
        } catch (e: JsonParseException) {
            null
        }
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @param cl
     * @return
     */
    @JvmStatic
    fun <T> jsonToBeanDateSerializer(jsonStr: String, cl: Class<T>): T? {
        return gson.fromJson(jsonStr, cl)
    }
}