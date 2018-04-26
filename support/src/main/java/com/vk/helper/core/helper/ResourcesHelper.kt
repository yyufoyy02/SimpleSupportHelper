@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core.helper

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.util.DisplayMetrics
import com.vk.helper.core.AppContextHelper
import java.io.InputStream

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2018/4/24
 * @version V1.0.0 < 创建 >
 */
object ResourcesHelper {

    @JvmStatic
    @ColorInt
    inline fun getColor(@ColorRes id: Int): Int {
        return AppContextHelper.resources().getColor(id)
    }

    @JvmStatic
    inline fun getString(@StringRes id: Int): String {
        return AppContextHelper.resources().getString(id)
    }

    @JvmStatic
    inline fun getStringArray(@ArrayRes id: Int): Array<String> {
        return AppContextHelper.resources().getStringArray(id)
    }

    @JvmStatic
    inline fun getIntArray(@ArrayRes id: Int): IntArray {
        return AppContextHelper.resources().getIntArray(id)
    }

    @JvmStatic
    inline fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return AppContextHelper.resources().getString(id, formatArgs)
    }

    @JvmStatic
    inline fun getDrawable(@DrawableRes id: Int): Drawable {
        return AppContextHelper.resources().getDrawable(id)
    }

    @JvmStatic
    inline fun getColorStateList(@ColorRes id: Int): ColorStateList {
        return AppContextHelper.resources().getColorStateList(id)
    }

    @JvmStatic
    inline fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return AppContextHelper.resources().getDimensionPixelSize(id)
    }

    @JvmStatic
    inline fun getDimension(@DimenRes id: Int): Float {
        return AppContextHelper.resources().getDimension(id)
    }

    @JvmStatic
    inline fun getDisplayMetrics(): DisplayMetrics {
        return AppContextHelper.resources().displayMetrics
    }

    @JvmStatic
    inline fun openRaw(@RawRes id: Int): InputStream {
        return AppContextHelper.resources().openRawResource(id)
    }

    /**
     * 获取资源图片
     */
    @JvmStatic
    inline fun resourceToBitmap(@RawRes id: Int): Bitmap {
        return BitmapFactory.decodeStream(openRaw(id), null, BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
            inPurgeable = true
            inSampleSize = 1
            inInputShareable = true
        })
    }
}