package com.vk.helper.core.helper

import android.util.TypedValue
import com.vk.helper.core.application

/**
 * 常用单位转换的辅助类
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
object DensityHelper {

    private var multiple: Float = 1.0f

    fun setMultiple(multiple: Float) {
        this@DensityHelper.multiple = multiple
    }

    /**
     * 设备相对单位 dp 转 像素 px
     *
     * @param context 上下文对象 context，用于获取 resource 实例
     * @param dpSize 设备相对单位 dp 值
     * @return 转换后的像素值
     */
    fun dp2px(dpSize: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize * multiple, application().resources.displayMetrics).toInt()
    }

    /**
     * 像素 sp 转 设备相对单位 dp
     *
     * @param spSize 像素单位值
     * @return 转换后的设备相对单位 dp
     */
    fun sp2px(spSize: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spSize * multiple, application().resources.displayMetrics).toInt()
    }

    /**
     * 像素 px 转 设备相对单位 dp
     *
     * @param pixelSize 像素单位值
     * @return 转换后的设备相对单位 dp
     */
    fun px2dp(pixelSize: Float): Int {
        val scale = application().resources.displayMetrics.density
        return (pixelSize / scale + 0.5f).toInt()
    }

    /**
     * 像素 px 转 字体相对单位 sp
     *
     * @param pixelSize 像素单位值
     * @return 转换后的字体相对单位 sp
     */
    fun px2sp(pixelSize: Float): Float {
        return pixelSize / application().resources.displayMetrics.scaledDensity
    }
}