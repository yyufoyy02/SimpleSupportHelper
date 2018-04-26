package com.vk.helper.core.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.vk.helper.core.AppContextHelper
import java.lang.reflect.InvocationTargetException

@SuppressLint("CommitPrefEdits")
/**
 * SharePreferences存储工具类
 *
 * @author Viking
 * @date 2018/4/23
 * @version V1.0.0 < 创建 >
 */
object SharePreferencesHelper {

    private val sharedPreferences by lazy { AppContextHelper.application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE) }
    //保存在手机里面的文件名
    private const val FILE_NAME = "vk_data"

    /**
     * 存储数据
     * @param key 存储key
     * @param value 存储值
     */
    @JvmStatic
    fun <T> putValue(key: String, value: T) {
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> editor.putString(key, value.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 获取对应的保存的值
     * @param key 存储key
     * @param default 默认值
     * @return 存储的值
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> getValue(key: String, default: T): T {
        return when (default) {
            is String -> sharedPreferences.getString(key, default) as T
            is Int -> sharedPreferences.getInt(key, default) as T
            is Boolean -> sharedPreferences.getBoolean(key, default) as T
            is Float -> sharedPreferences.getFloat(key, default) as T
            is Long -> sharedPreferences.getLong(key, default) as T
            else -> sharedPreferences.getString(key, default.toString()) as T
        }
    }

    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    @JvmStatic
    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 返回所有的键值对
     * @return
     */
    @JvmStatic
    fun getAll(): Map<String, *> {
        return sharedPreferences.all
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return 是否存在
     */
    @JvmStatic
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    /**
     * 清除所有数据
     */
    @JvmStatic
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private object SharedPreferencesCompat {

        //反射查找apply的方法
        private val sApplyMethod by lazy {
            try {
                val clz = SharedPreferences.Editor::class.java
                return@lazy clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return@lazy null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                sApplyMethod?.invoke(editor)
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
            editor.commit()
        }
    }
}