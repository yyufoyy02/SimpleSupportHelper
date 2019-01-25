package com.viking.vikingdemo

import android.util.Log
import com.vk.helper.core.AppContextHelper

/**
 * 刘海屏辅助类
 *
 * @author Viking
 * @date 2018/6/15
 * @version V1.0.0 < 创建 >
 */
object NotchInScreenHelper : NotchInScreen {

    private val notchInScreen: NotchInScreen? =
            when (RomHelper.getSysRom()) {
                is OppoRom -> {
                    OppoNotchInScreen()
                }
                is VivoRom -> {
                    VivoNotchInScreen()
                }
                else -> {
                    null
                }
            }

    override fun hasNotch() = notchInScreen?.hasNotch() == true
}

/**
 * Oppo 刘海屏实现类
 *
 * @author viking
 * @date 2018/6/20
 */
class OppoNotchInScreen : NotchInScreen {

    override fun hasNotch(): Boolean {
        return AppContextHelper.application.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }
}

/**
 * Vivo 刘海屏实现类
 *
 * @author viking
 * @date 2018/6/20
 */
class VivoNotchInScreen : NotchInScreen {

    val NOTCH_IN_SCREEN_VOIO = 0x00000020//是否有凹槽
    val ROUNDED_IN_SCREEN_VOIO = 0x00000008//是否有圆角

    override fun hasNotch(): Boolean {
        var ret = false
        try {
            val cl = AppContextHelper.application.classLoader
            val ftFeature = cl.loadClass("com.util.FtFeature")
            val get = ftFeature.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            ret = get.invoke(ftFeature, NOTCH_IN_SCREEN_VOIO) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("test", "hasNotchInScreen Exception")
        } finally {
            return ret
        }
    }
}

interface NotchInScreen {
    fun hasNotch(): Boolean
}
