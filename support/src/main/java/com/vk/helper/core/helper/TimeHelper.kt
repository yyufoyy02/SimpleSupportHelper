package com.vk.helper.core.helper

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间戳工具类
 *
 * @author Viking
 * @date 2018/4/23
 * @version V1.0.0 < 创建 >
 */
object TimeHelper {

    const val FORMAT_DATE = "yyyy年MM月dd日 HH:mm"
    const val FORMAT_DATE2 = "yyyy-MM-dd HH:mm"
    const val FORMAT_TIME = "HH:mm"
    const val FORMAT_DATE_TIME = "yyyy年MM月dd日"
    const val FORMAT_DATE_TIME2 = "yyyy/MM/dd"
    const val FORMAT_MONTH_DAY_TIME = "MM月dd日 HH:mm"
    const val FORMAT_DATE_2 = "yyyy.MM.dd HH:mm"

    private const val YEAR = 365 * 24 * 60 * 60// 年
    private const val MONTH = 30 * 24 * 60 * 60// 月
    private const val DAY = 24 * 60 * 60// 天
    private const val HOUR = 60 * 60// 小时
    private const val MINUTE = 60// 分钟
    private const val SECONDS_IN_DAY = 60 * 60 * 24
    private const val MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY

    private val calendar: Calendar by lazy { Calendar.getInstance() }
    private val dateFormat: SimpleDateFormat by lazy { SimpleDateFormat() }

    /**
     * 将时间戳以指定格式转换为日期时间字符串
     *
     * @param time      时间戳 单位为秒
     * @param timeStyle 指定的日期时间格式，例如"yyyy-MM-dd HH:mm"
     * @return HH:mm 24小时制  hh:mm 12小时制
     */
    @JvmStatic
    fun getFormatTimeFromTimestamp(timestamp: Long, format: String = FORMAT_DATE): String {
        if (timestamp == 0L) {
            return ""
        }
        calendar.timeInMillis = timestamp * 1000
        dateFormat.applyPattern(format)
        return dateFormat.format(calendar.time).toString()
    }

    /**
     *
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     * @param timestamp 时间戳 单位为秒
     * @return 时间字符串
     */
    @JvmStatic
    fun getDescriptionTimeFromTimestamp(timestamp: Long): String {
        val times = timestamp * 1000
        val currentTime = System.currentTimeMillis()
        var timeGap = (currentTime - times) / 1000// 与现在时间相差秒数
        val nowCalendar = Calendar.getInstance()
        val msgCalendar = Calendar.getInstance()
        msgCalendar.timeInMillis = times
        val timeStr = StringBuffer()
        timeStr.setLength(0)
        timeStr.append(getFormatTimeFromTimestamp(timestamp))
        if (timeGap >= 0) {
            if (timeGap > YEAR) {
                //一年前
            } else if (timeGap > MONTH) {
                //一个月前
            } else if (timeGap > DAY) {
                // 1天以上
                val timeDay = timeGap / DAY
                if (isYesterDay(nowCalendar, msgCalendar)) {
                    timeStr.setLength(0)
                    timeStr.append("昨天${getFormatTimeFromTimestamp(timestamp, "HH:mm")}")
                }
            } else if (timeGap > HOUR) {
                // 1小时-24小时
                if (isSameDay(nowCalendar, msgCalendar)) {
                    timeStr.setLength(0)
                    timeStr.append("${timeGap / HOUR}小时前")
                } else {
                    timeStr.setLength(0)
                    timeStr.append("昨天${getFormatTimeFromTimestamp(timestamp, "HH:mm")}")
                }
            } else if (timeGap > MINUTE) {
                // 1分钟-59分钟
                timeStr.setLength(0)
                timeStr.append("${timeGap / MINUTE}分钟前")
            } else {
                // 1秒钟-59秒钟
                timeStr.setLength(0)
                timeStr.append("刚刚")
            }
        } else {
            timeGap = -timeGap
            if (timeGap > YEAR) {
            } else if (timeGap > MONTH) {
            } else if (timeGap > DAY) {
                // 1天以上
                val timeDay = timeGap / DAY
                if (isTomorrowDay(nowCalendar, msgCalendar)) {
                    timeStr.setLength(0)
                    timeStr.append("明天${getFormatTimeFromTimestamp(timestamp, "HH:mm")}")
                }
            } else if (timeGap > HOUR) {// 1小时-24小时
                if (isSameDay(nowCalendar, msgCalendar)) {
                    timeStr.setLength(0)
                    timeStr.append("今天${getFormatTimeFromTimestamp(timestamp, "HH:mm")}")
                } else {
                    timeStr.setLength(0)
                    timeStr.append("明天${getFormatTimeFromTimestamp(timestamp, "HH:mm")}")
                }
            } else if (timeGap > MINUTE) {
                // 1分钟-59分钟
            } else {
                // 1秒钟-59秒钟
            }
        }
        return timeStr.toString()
    }

    /**
     * 判断是否同一半天
     */
    @JvmStatic
    fun isSameHalfDay(now: Calendar, msg: Calendar): Boolean {
        val nowHour = now.get(Calendar.HOUR_OF_DAY)
        val msgHOur = msg.get(Calendar.HOUR_OF_DAY)
        return if ((nowHour <= 12) and (msgHOur <= 12)) {
            true
        } else
            (nowHour >= 12) and (msgHOur >= 12)
    }

    /**
     * 判断是否同一天
     */
    @JvmStatic
    fun isSameDay(now: Calendar, msg: Calendar): Boolean {
        val nowDay = now.get(Calendar.DAY_OF_YEAR)
        val msgDay = msg.get(Calendar.DAY_OF_YEAR)
        return nowDay == msgDay
    }

    /**
     * 是否昨天
     */
    @JvmStatic
    fun isYesterDay(now: Calendar, msg: Calendar): Boolean {
        val nowDay = now.get(Calendar.DAY_OF_YEAR)
        val msgDay = msg.get(Calendar.DAY_OF_YEAR)

        return nowDay - msgDay == 1
    }

    /**
     * 是否明天
     */
    @JvmStatic
    fun isTomorrowDay(now: Calendar, msg: Calendar): Boolean {
        val nowDay = now.get(Calendar.DAY_OF_YEAR)
        val msgDay = msg.get(Calendar.DAY_OF_YEAR)

        return nowDay - msgDay == -1
    }

    /**
     * 相差天数
     * @param now
     * @param msg
     * @return
     */
    @JvmStatic
    fun getDayMinusCount(now: Calendar, msg: Calendar): Int {
        val nowDay = now.get(Calendar.DAY_OF_YEAR)
        val msgDay = msg.get(Calendar.DAY_OF_YEAR)

        return Math.abs(nowDay - msgDay)
    }
}