package com.vk.helper.core.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.support.annotation.StringRes
import android.view.Gravity
import android.widget.Toast

import com.vk.helper.core.AppContextHelper

@SuppressLint("ShowToast")
/**
 * Toast 辅助类
 *
 * @author Viking
 * @date 2018/4/20
 * @version V1.0.0 < 创建 >
 */
object ToastHelper {
    const val DEFAULT_DURATION = 1500

    private val toast: Toast by lazy {
        Toast.makeText(AppContextHelper.application, "", Toast.LENGTH_SHORT)
    }

    private val toastHandler by lazy { Handler() }
    private val cancelRunnable by lazy { Runnable { toast.cancel() } }

    @JvmOverloads
    @JvmStatic
    fun showToast(text: String?, duration: Int = DEFAULT_DURATION) {
        if (!text.isNullOrEmpty()) {
            toastHandler.removeCallbacks(cancelRunnable)
            toast.setText(text)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toastHandler.postDelayed(cancelRunnable, duration.toLong())
            toast.show()
        }
    }

    @JvmOverloads
    @JvmStatic
    fun showToast(@StringRes resId: Int, duration: Int = DEFAULT_DURATION) {
        showToast(AppContextHelper.resources().getString(resId), duration)
    }
}

