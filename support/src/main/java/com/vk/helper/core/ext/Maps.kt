@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core.ext

import android.util.ArrayMap

/**
 * Created by liangweijian on 2018/1/22.
 */
/**
 * 复制新的字典
 */
inline fun Map<String, String>?.copyNotNull(): MutableMap<String, String> {
    val map: MutableMap<String, String> = HashMap()
    if (this != null) {
        map.putAll(this)
    }
    return map
}

/**
 * 字典添加非空字典
 * 如果增加的数据是NULL，则返回false不作处理
 */
inline fun <K, V> MutableMap<K, V>.elementPutAllFilterNull(t: Map<K, V>?): Boolean {
    if (t == null) {
        return false
    }
    this.putAll(t)
    return true
}

inline fun <K, V> arrayMapOf(): HashMap<K, V> = HashMap()

fun <K, V> arrayMapOf(vararg pairs: Pair<K, V>): HashMap<K, V> {
    val map = HashMap<K, V>(pairs.size)
    for (pair in pairs) {
        map[pair.first] = pair.second
    }
    return map
}

inline fun <K, V> supportArrayMapOf(): HashMap<K, V> = HashMap()

fun <K, V> supportArrayMapOf(vararg pairs: Pair<K, V>): HashMap<K, V> {
    val map = HashMap<K, V>(pairs.size)
    for (pair in pairs) {
        map[pair.first] = pair.second
    }
    return map
}
