@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core.ext

/**
 * Created by liangweijian on 2018/1/9.
 */

/**
 * 判断数组是否为空或空数组
 */
inline fun Collection<*>?.isNullOrEmpty(): Boolean = this == null || isEmpty()


/**
 * 判断数组是否为空或空数组
 */
inline fun Collection<*>?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

/**
 * collections find 拓展的升级版，有 index
 * @param predicate 循环传入 index 和 element，找是否存在符合条件的元素
 * @return 根据条件找到返回 true
 */
inline fun <T> Iterable<T>.findIndexed(predicate: (index: Int, T) -> Boolean): Boolean {
    this.forEachIndexed { index, element ->
        if (predicate(index, element)) return true
    }
    return false
}

/**
 * 删除数据越界判断
 * 如果越界返回NULL
 */
inline fun <T> MutableList<T>.elementRemoveOrNull(index: Int): T? {
    return if (index in 0..lastIndex) removeAt(index) else null
}

/**
 * 数组添加非空数据
 * 如果增加的数据是NULL，则返回false不作处理
 */
inline fun <T> MutableList<T>.elementAddFilterNull(t: T?): Boolean {
    if (t == null) {
        return false
    }
    return add(t)
}

/**
 * 数组添加非空数组
 * 如果增加的数据是NULL，则返回false不作处理
 */
inline fun <T> MutableList<T>.elementAddAllFilterNull(t: List<T>?): Boolean {
    if (t == null) {
        return false
    }
    return addAll(t)
}

/**
 * 数组添加非空数据
 * 如果增加的数据是NULL或者index越界，则不作处理
 */
inline fun <T> MutableList<T>.elementAddFilterNull(index: Int, t: T?) {
    if (t == null || index > size) return
    add(index, t)
}
