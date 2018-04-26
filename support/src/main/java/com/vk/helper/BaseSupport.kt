@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper

import android.app.Application
import com.vk.helper.core.AppContextHelper
import com.vk.helper.core.AppContextHelper.application
import com.vk.helper.core.helper.AppHelper
import com.vk.helper.core.helper.DensityHelper

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2018/4/26
 * @version V1.0.0 < 创建 >
 */
object BaseSupport {

    /**
     * 是否debug模式
     */
    var DEBUG: Boolean = false

    inline fun initConfig(block: Builder.() -> Unit) {
        val builder = Builder.build(block)
        builder.application?.let {
            AppContextHelper.init(it)
        }
        if (builder.multiple != 0.0f) {
            DensityHelper.setMultiple(builder.multiple)
        }
        DEBUG = builder.debug
    }

    class Builder {
        var application: Application? = null
        var multiple: Float = 1.0f
        var debug: Boolean = false

        fun build() = this

        companion object {
            inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
        }
    }
}