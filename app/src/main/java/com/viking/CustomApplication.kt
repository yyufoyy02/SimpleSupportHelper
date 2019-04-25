package com.viking

import android.app.Application
import android.content.Context
import com.vk.helper.core.AppContextHelper

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2019/4/25
 * @version V1.0.0 < 创建 >
 */
 class MJLApplication : Application() {

     override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        AppContextHelper.init(this)
    }

}