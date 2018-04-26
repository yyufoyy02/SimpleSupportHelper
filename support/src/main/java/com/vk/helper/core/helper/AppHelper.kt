@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core.helper

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import com.vk.helper.core.application

/**
 * 应用辅助类
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
object AppHelper {

    private var lastClickTime: Long = 0
    /**
     * 判断当前应用程序处于前台还是后台
     */
    @JvmStatic
    inline fun isApplicationBroughtToBackground(): Boolean {
        val am = application().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName != application().packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 控件是否被快速点击
     */
    @JvmStatic
    fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (timeD in 1..799) {
            return true
        }
        lastClickTime = time
        return false
    }

    /**
     * 获取当前系统的最上层 activity 栈的第一个 activity 的组件对象
     *
     * @return 组件对象
     */
    @JvmStatic
    inline fun getTopComponent(): ComponentName? {
        val result: ComponentName? = null
        try {
            val activityManager = application().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // 仍然可以返回自己 app 的 task
            val list = activityManager.getRunningTasks(1)
            if (list == null || list.size <= 0) {
                return null
            }
            val activityTask = list[0]
            if (activityTask != null) {
                return activityTask.topActivity
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 获取当前系统的最上层 activity 栈的第一个 activity 的组件名
     *
     * @return true 表示是前台显示
     */
    @JvmStatic
    inline fun isForegroundRunning(): Boolean {
        val component = getTopComponent() ?: return false
        return component.packageName == application().packageName
    }

    /**
     * 返回当前 app 是否为前台显示
     *
     * @return 获取的组件对象的名称
     */
    @JvmStatic
    inline fun getTopComponentClassName(): String {
        val component = getTopComponent()
        return if (component != null) {
            component.className
        } else ""
    }
}