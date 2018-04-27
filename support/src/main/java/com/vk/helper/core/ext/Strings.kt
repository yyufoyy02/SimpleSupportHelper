@file:JvmName("StringHelper")
@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core.ext

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import com.vk.helper.core.helper.ResourcesHelper
import java.security.MessageDigest
import kotlin.experimental.and

/**
 * 字符串工具类
 *
 * @author Viking
 * @date 2018/4/24
 * @version V1.0.0 < 创建 >
 */

/**
 * 转换成MD5
 */
inline fun String.toMd5(): String {
    return try {
        MessageDigest.getInstance("MD5").let {
            it.update(this.toByteArray())
            it.digest().toHexString()
        }
    } catch (e: Exception) {
        ""
    }
}

/**
 * byte数组转成十六进制字符串
 */
private val hexChar = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

@Throws(Exception::class)
fun ByteArray.toHexString(): String {
    val sb = StringBuilder(this.size * 2)
    for (i in this.indices) {
        sb.append(hexChar[(this[i] and 0xf0.toByte()).toInt().ushr(4)])
        sb.append(hexChar[(this[i] and 0x0f).toInt()])
    }
    return sb.toString()
}

/**
 * 设置部分字体颜色下划线
 */
inline fun String.toColorSpannableString(begin: Int, end: Int, color: Int, under: Boolean = false): SpannableStringBuilder {
    val style = SpannableStringBuilder(this)
    if (begin >= 0 && end >= 0 && begin != end) {
        if (under) {
            style.setSpan(UnderlineSpan(), begin, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        style.setSpan(ForegroundColorSpan(ResourcesHelper.getColor(color)), begin, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return style
}

/**
 * 创建String字符串
 */
inline fun createString(block: StringBuilder.() -> Unit): String {
    val sb = StringBuilder()
    sb.block()
    return sb.toString()
}