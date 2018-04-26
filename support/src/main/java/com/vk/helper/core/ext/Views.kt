@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("ViewHelper")


package com.vk.helper.core.ext

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Rect
import android.support.annotation.IntDef
import android.view.TouchDelegate
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.vk.helper.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * View辅助类
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
/**
 * Content装换成Activity
 */
inline fun View.getActivity(): Activity? {
    if (context == null) {
        return null
    }
    if (context is Activity) {
        return context as Activity?
    }
    if (context is ContextWrapper) {
        return (context as ContextWrapper).baseContext as Activity?
    }
    return null
}

/**
 * 增加控件点击范围
 */
inline fun View.expandViewTouchDelegate(addition: Int) {
    (parent as View).post {
        val bounds = Rect()
        isEnabled = true
        getHitRect(bounds)
        bounds.top = bounds.top - addition
        bounds.bottom = bounds.bottom + addition
        bounds.left = bounds.left - addition
        bounds.right = bounds.right + addition
        val touchDelegate = TouchDelegate(bounds, this)
        if (View::class.java.isInstance(parent)) {
            (parent as View).touchDelegate = touchDelegate
        }
    }
}

/**
 * 添加到ViewGroup
 */
inline fun View.addToParent(viewGroup: ViewGroup) = viewGroup.addView(this)

inline fun View.addToParent(viewGroup: ViewGroup, index: Int) = viewGroup.addView(this, index)

inline fun View.addToParent(viewGroup: ViewGroup, width: Int, height: Int) = viewGroup.addView(this, width, height)

inline fun View.addToParent(viewGroup: ViewGroup, params: ViewGroup.LayoutParams) = viewGroup.addView(this, params)

/**
 * Executes [block] with the View's layoutParams and reassigns the layoutParams with the
 * updated version.
 *
 * @see View.getLayoutParams
 * @see View.setLayoutParams
 **/
inline fun View.updateLayoutParams(block: ViewGroup.LayoutParams.() -> Unit) {
    updateLayoutParams<ViewGroup.LayoutParams>(block)
}

/**
 * Executes [block] with a typed version of the View's layoutParams and reassigns the
 * layoutParams with the updated version.
 *
 * @see View.getLayoutParams
 * @see View.setLayoutParams
 **/
@JvmName("updateLayoutParamsTyped")
inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParams(block: T.() -> Unit) {
    val params = layoutParams as T
    block(params)
    layoutParams = params
}

/**
 * 为一组 view 设置统一的监听事件，常用于 ConstraintLayout
 *
 * @param listener 点击监听事件
 */
fun List<View?>.setClickListener(listener: View.OnClickListener) {
    filterNotNull().forEach {
        it.setOnClickListener(listener)
    }
}

/**
 * 为一组 view 改变 visibility
 *
 * @param visibility View 的可见性设置
 */
@Suppress("DEPRECATED_JAVA_ANNOTATION")
@IntDef(VISIBLE, INVISIBLE, GONE)
@Retention(RetentionPolicy.SOURCE)
annotation class Visibility

fun List<View?>.setVisibility(@Visibility visibility: Int) {
    filterNotNull().forEach {
        it.visibility = visibility
    }
}

/**
 * 是否有效点击
 * 两次点击间隔500毫秒以上
 *
 */
fun View.isValidClick(): Boolean {
    val tempObject = getTag(R.id.tag_last_click_time)
    val currentTime = System.currentTimeMillis()
    setTag(R.id.tag_last_click_time, currentTime)
    return if (tempObject == null || tempObject !is Long) {
        true
    } else {
        currentTime - tempObject >= 500
    }
}