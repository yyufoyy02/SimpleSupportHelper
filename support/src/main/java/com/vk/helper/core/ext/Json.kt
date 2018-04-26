@file:JvmName("JsonHelper")

package com.vk.helper.core.ext

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.vk.helper.core.helper.GsonHelper
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

/**
 * 将java对象转换成json字符串
 *
 * @param obj 准备转换的对象
 * @return json字符串
 * @throws Exception
 */
fun Any.toJson(): String {
    return GsonHelper.objectToJson(this)
}

/**
 * json字符串转化为 JavaBean
 * @param content
 * @param valueBean
 * @param <T>
 * @return
 */
inline fun <reified T> JSONObject?.toJavaBean(): T? {
    return toString().toJavaBean()
}

inline fun <reified T> String?.toJavaBean(): T? {
    if (this == null) {
        return null
    }
    var bean: T? = null
    try {
        bean = if (bean is List<*>) {
            GsonHelper.gson.fromJson(this, object : com.google.gson.reflect.TypeToken<T>() {}.type)
        } else {
            GsonHelper.jsonToBean(this, T::class.java)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bean
}