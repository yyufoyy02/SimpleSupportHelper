@file:Suppress("NOTHING_TO_INLINE")

package com.vk.helper.core

import com.vk.helper.core.helper.*
import java.text.DecimalFormat

/**
 * 扩展方法
 * @author viking
 * @date 2018/4/24
 */

/**
 * application
 */
inline fun application() = AppContextHelper.application

/**
 * 屏幕辅助类
 */
inline fun getScreenWidth() = ScreenHelper.getScreenWidth()

inline fun getScreenHeight() = ScreenHelper.getScreenHeight()

/**
 * 时间辅助类(单位秒)
 */
inline fun getSystemTimestamp() = System.currentTimeMillis() / 1000

/*显示日期时间字符串*/
inline fun Long.showFormatTime(format: String = TimeHelper.FORMAT_DATE) = TimeHelper.getFormatTimeFromTimestamp(this, format)

/**
 * logger
 */
inline fun loggerD(message: String?) {
    LoggerHelper.log().d(message)
}

inline fun loggerE(message: String?) {
    LoggerHelper.log().e(message)
}

inline fun loggerW(message: String?) {
    LoggerHelper.log().w(message)
}

inline fun loggerI(message: String?) {
    LoggerHelper.log().i(message)
}

inline fun loggerV(message: String?) {
    LoggerHelper.log().v(message)
}
/**
 * 转换成DP
 */
inline fun Float.toDp() = DensityHelper.dp2px(this)
/**
 * App信息
 */
/*设备Id*/
inline fun getDeviceId() = DeviceHelper.getDeviceId()

/**
 * Double
 */
/*保留指定位数小数，默认保留两位小数*/
inline fun Double.fractionDigits(num: Int = 2): String {
    val mDecimalFormat = DecimalFormat.getInstance()
    mDecimalFormat.maximumFractionDigits = num
    mDecimalFormat.minimumFractionDigits = num
    return mDecimalFormat.format(this).replace(",", "")
}
