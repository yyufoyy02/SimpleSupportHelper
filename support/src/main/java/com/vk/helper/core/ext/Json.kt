@file:JvmName("JsonHelper")

package com.vk.helper.core.ext

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.vk.helper.core.helper.GsonHelper
import com.vk.helper.core.loggerI
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
fun Any?.toJson(): String {
    if (this == null) {
        return ""
    }
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
        loggerI("T:${ T::class.java.name}")
        bean = if (T::class.java.name =="java.util.List") {
            loggerI("BeanList")
            GsonHelper.gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
        } else {
            loggerI("Bean")
            GsonHelper.jsonToBean(this, T::class.java)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bean
}