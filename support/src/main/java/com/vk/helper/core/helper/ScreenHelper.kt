package com.vk.helper.core.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import com.vk.helper.core.AppContextHelper

/**
 * 屏幕辅助类
 *
 * @author Viking
 * @date 2018/4/23
 * @version V1.0.0 < 创建 >
 */
object ScreenHelper {

    private val windowManager: WindowManager by lazy { AppContextHelper.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    /**
     * 获得屏幕宽度
     * @return 屏幕宽度像素值
     */
    @JvmStatic
    fun getScreenWidth(): Int = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }.widthPixels

    /**
     * 获得屏幕高度
     * @return 屏幕高度像素值
     */
    @JvmStatic
    fun getScreenHeight(): Int = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }.heightPixels

    /**
     * 获取窗口宽度
     * @param activity
     * @return 窗口宽度像素值
     */
    @JvmStatic
    fun getWindowWidth(activity: Activity): Int = Rect().apply {
        activity.window.decorView.getWindowVisibleDisplayFrame(this)
    }.width()

    /**
     * 获取窗口高度
     * @param activity
     * @return 窗口高度像素值
     */
    @JvmStatic
    fun getWindowHeight(activity: Activity): Int = Rect().apply {
        activity.window.decorView.getWindowVisibleDisplayFrame(this)
    }.height()

    /**
     * 获得状态栏的高度
     * @return 状态栏高度像素值
     */
    @SuppressLint("PrivateApi")
    @JvmStatic
    fun getStatusHeight(context: Context = AppContextHelper.application): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    /**
     * 获得底部工具栏的高度
     * @return 底部工具栏高度像素值
     */
    @JvmStatic
    fun getNavigationBarHeight(): Int = if (isNavigationBarShow()) {
        AppContextHelper.resources().run {
            getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
        }
    } else {
        0
    }

    /**
     * 底部工具栏是否显示
     */
    @JvmStatic
    fun isNavigationBarShow(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val display = windowManager.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        realSize.y != size.y
    } else {
        val menu = ViewConfiguration.get(AppContextHelper.application).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        !(menu || back)
    }

    /**
     * 获取百分比的屏幕宽度
     */
    @JvmStatic
    fun getWidthByScreenProportion(percentage: Float, minWidth: Int = 0): Int =
            (percentage * getScreenWidth()).toInt().let {
                if (it > minWidth) it else minWidth
            }

    /**
     * 获取百分比的屏幕高度
     */
    @JvmStatic
    fun getHeightByScreenProportion(percentage: Float, minHeight: Int = 0): Int =
            (percentage * getScreenHeight()).toInt().let {
                if (it > minHeight) it else minHeight
            }
}


