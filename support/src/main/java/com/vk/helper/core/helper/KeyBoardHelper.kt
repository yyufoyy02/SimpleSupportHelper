package com.vk.helper.core.helper

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.vk.helper.core.application

/**
 * 键盘辅助类
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
object KeyBoardHelper {

    private val inputMethodManager by lazy { application().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    /**
     * 打开软键盘
     *
     * @param editText 输入框打开键盘
     */
    @JvmStatic
    fun openKeyboard(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val isFocus = editText.isFocused
        Handler().postDelayed({
            if (isFocus) {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            } else {
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            }
        }, 200)
    }

    /**
     * 关闭软键盘
     *
     * @param activity 页面
     */
    @JvmStatic
    fun closeKeyboard(activity: Activity) {
        try {
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: NullPointerException) {
        }
        return
    }

    /**
     * 键盘是否打开
     *
     */
    @JvmStatic
    fun isOpenKeyboard(mEditText: EditText): Boolean {
        try {
            return inputMethodManager.hideSoftInputFromWindow(mEditText.windowToken, 0)
        } catch (e: NullPointerException) {
        }
        return false
    }

    private var sLastVisible = false
    /**
     * 监听软键盘状态
     *
     * @param activity
     * @param lastVisible 键盘显示状态
     * @param listener 监听
     */
    @JvmStatic
    fun addOnSoftKeyBoardVisibleListener(activity: Activity, lastVisible: Boolean = false, listener: ((Boolean) -> Unit)? = null) {
        val decorView = activity.window.decorView
        sLastVisible = lastVisible
        decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            decorView.getWindowVisibleDisplayFrame(rect)
            val displayHeight = rect.bottom - rect.top
            val height = decorView.height
            val visible = displayHeight.toDouble() / height < 0.8
            if (visible != sLastVisible) {
                listener?.invoke(visible)
            }
            sLastVisible = visible
        }
    }
}