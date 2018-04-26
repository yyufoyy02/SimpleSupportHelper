@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import com.vk.helper.core.application
import com.vk.helper.core.helper.uuid.DeviceIdFactory
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * 设备信息辅助类
 *
 * @author Viking
 * @date 2018/4/24
 * @version V1.0.0 < 创建 >
 */
object DeviceHelper {

    /**
     * 获取唯一androidId
     */
    @JvmStatic
    @SuppressLint("HardwareIds")
    inline fun getDeviceAndroidId() = Settings.Secure.getString(application().contentResolver, Settings.Secure.ANDROID_ID)

    /**
     * 获取唯一DeviceId
     * 在旧版androidId有可能为空或者等于9774d56d682e549c，当这两种情况下使用deviceId
     * @return androidId -> deviceId
     */
    @JvmStatic
    inline fun getDeviceId() = DeviceIdFactory(application()).deviceId

    /**
     * 获取设备品牌
     */
    @JvmStatic
    inline fun getBrand(): String {
        return Build.BRAND
    }

    /**
     * 获取设备型号
     */
    @JvmStatic
    inline fun getModel(): String {
        return Build.MODEL
    }

    /**
     * OS版本号
     */
    @JvmStatic
    inline fun getRelease(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 屏幕分辨率
     */
    @JvmStatic
    inline fun getResolution(activity: Activity): String {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels.toString() + "x" + dm.widthPixels
    }

    /**
     * 获取总运存大小
     */
    @JvmStatic
    inline fun getTotalMemory(): Long {
        val str1 = "/proc/meminfo"// 系统内存信息文件
        val str2: String
        val arrayOfString: Array<String>
        var initialMemory: Long = 0
        try {
            val localBufferedReader = BufferedReader(FileReader(str1), 8192)
            str2 = localBufferedReader.readLine()// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            initialMemory = (Integer.valueOf(arrayOfString[1]).toInt() * 1024).toLong()// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close()
        } catch (e: IOException) {
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        //        System.out.println("总运存--->>>"+initial_memory/(1024*1024));
        return initialMemory / (1024 * 1024)
    }

    /**
     * 获取应用程序名称
     */
    @JvmStatic
    inline fun getAppName(): String {
        try {
            val packageInfo = application().packageManager.getPackageInfo(application().packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return application().resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @return 当前应用的版本名称
     */
    @JvmStatic
    inline fun getVersionName(): String {
        try {
            val packageInfo = application().packageManager.getPackageInfo(application().packageName, 0) ?: return ""
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取版本号(内部识别号)
     */
    @JvmStatic
    inline fun getVersionCode(): Int {
        try {
            return application().packageManager.getPackageInfo(application().packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取系统语言
     */
    @JvmStatic
    inline fun getMultiLanguage(): Locale? {
        return application().resources.configuration.locale
    }

    @JvmStatic
    inline fun setMultiLanguage(locale: Locale) {
        val config = application().resources.configuration
        config.locale = locale
        application().resources.updateConfiguration(config, application().resources.displayMetrics)
    }

    /**
     * 获取包名
     */
    @JvmStatic
    inline fun getPackageInfo(): PackageInfo {
        val pm = application().packageManager
        try {
            return pm.getPackageInfo(application().packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return PackageInfo()
    }

    /**
     * 判断手机上是否安装了目标APP
     */
    @JvmStatic
    inline fun isPackageAvailable(packageName: String): Boolean {
        val packageInfo = application().packageManager.getInstalledPackages(0) ?: return false
        for (i in packageInfo.indices) {
            val pn = packageInfo[i].packageName
            if (pn == packageName) {
                return true
            }
        }
        return false
    }
}