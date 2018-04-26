package com.vk.helper.core

import android.app.Application
import android.content.res.AssetManager
import android.content.res.Resources

/**
 * Context 管理类
 *
 * @author Viking
 * @date 2018/4/20
 * @version V1.0.0 < 创建 >
 */
object AppContextHelper {

    @JvmStatic
    lateinit var application: Application

    /**
     * 在其他方法调用之前都要 init ，在 Application 中实例
     */
    @JvmStatic
    fun init(context: Application) {
        application = context
    }

    @JvmStatic
    fun resources(): Resources {
        return application.resources
    }

    @JvmStatic
    fun assets(): AssetManager {
        return application.assets
    }
}