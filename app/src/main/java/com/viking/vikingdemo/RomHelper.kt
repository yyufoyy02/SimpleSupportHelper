package com.viking.vikingdemo;

import android.os.Build
import com.vk.helper.core.loggerE
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 获取系统Rom辅助类
 *
 * @author viking
 * @date 2018/6/20
 */
object RomHelper {

    private var rom: Rom? = null

    /**
     * 获取系统版本rom
     *
     * @author viking
     * @date 2018/6/20
     */
    fun getSysRom(): Rom {
        return rom ?: arrayListOf(VivoRom(),SmartisamRom(),FlymeRom(),OppoRom(), EmuiRom(), MiuiRom())
                .firstOrNull {
                    val propName = it.propName()
                    if (propName.isNotEmpty() && !getProp(propName).isNullOrEmpty()) {
                        true
                    } else {
                        Build.DISPLAY.toUpperCase().contains(it.romName())
                    }
                }.let {
                    rom = it ?: UnknownRom()
                    rom!!
                }
    }

    /**
     * 通过系统配置获取对应的rom信息
     *
     * @param name 路径
     * @author viking
     * @date 2018/6/20
     */
    private fun getProp(name: String): String? {
        var line: String? = null
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            loggerE("Unable to read prop $name,${ex.message}")
            return null
        } finally {
            try {
                input?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return line
    }
}

/**
 * Vivo rom
 *
 * @author viking
 * @date 2018/6/20
 */
class VivoRom : Rom(){
    override fun romName() = "VIVO"

    override fun propName() = "ro.vivo.os.version"
}

/**
 * 锤子 rom
 *
 * @author viking
 * @date 2018/6/20
 */
class  SmartisamRom :Rom(){

    override fun romName() = "SMARTISAN"

    override fun propName()  = "ro.smartisan.version"
}

/**
 * Oppo rom
 *
 * @author viking
 * @date 2018/6/20
 */
class OppoRom :Rom(){
    override fun romName() = "OPPO"

    override fun propName() = "ro.build.version.opporom"
}

/**
 * 魅族 rom
 *
 * @author viking
 * @date 2018/6/20
 */
class FlymeRom : Rom() {

    override fun romName() = "FLYME"

    override fun propName(): String = ""
}

/**
 * 华为 rom
 *
 * @author viking
 * @date 2018/6/20
 */
class EmuiRom : Rom() {

    override fun romName() = "EMUI"

    override fun propName() = "ro.build.version.emui"
}

/**
 * 小米 rom
 *
 * @author viking
 * @date 2018/6/20
 */
class MiuiRom : Rom() {

    override fun romName() = "MIUI"

    override fun propName() = "ro.miui.ui.version.name"
}

/**
 * 未知 rom
 *
 * @author viking
 * @date 2018/6/20
 */
class UnknownRom : Rom() {
    override fun romName() = "UNKNOWN"

    override fun propName(): String = ""
}

sealed class Rom {
     abstract fun romName(): String

     abstract fun propName(): String
}
