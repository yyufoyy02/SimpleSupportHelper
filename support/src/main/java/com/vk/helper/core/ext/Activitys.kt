@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("ActivityHelper")

package com.vk.helper.core.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.ColorRes
import com.vk.helper.BaseSupport
import com.vk.helper.core.helper.ResourcesHelper
import com.vk.helper.core.loggerE
import com.vk.helper.core.showToast
import java.io.Serializable

/**
 * 修改状态栏颜色，支持4.4以上版本
 *
 * @param activity
 * @param colorRid
 */
inline fun Activity.setStatusBarColorResource(@ColorRes colorRid: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.statusBarColor = ResourcesHelper.getColor(colorRid)
    } else {
        if (BaseSupport.DEBUG) {
            showToast("Android 6.0 以上允许设置状态栏颜色")
        } else {
            loggerE("Android 6.0 以上允许设置状态栏颜色")
        }
    }
}

/**
 * 页面跳转方法
 * @param params Intent里面的参数
 */
inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    FillIntent.fillIntentArguments(intent = intent, params = params)
    this.startActivity(intent)
}

/**
 * 页面跳转方法
 * @param params Intent里面的参数
 * @param requestCode
 * 兼容旧的逻辑，对requestCoder == NO_REQUEST_CODE特殊处理，不调用startActivityForResult，不然会报错
 */
inline fun <reified T : Activity> Activity.startActivityForResult(vararg params: Pair<String, Any?>, requestCode: Int) {
    val intent = Intent(this, T::class.java)
    FillIntent.fillIntentArguments(intent = intent, params = params)
    startActivityForResult(intent, requestCode)
}

object FillIntent {
    fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            val value = it.second
            when (value) {
                null -> intent.putExtra(it.first, null as Serializable?)
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }
}
