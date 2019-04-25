package com.viking.vikingdemo

import android.content.Context
import android.content.pm.ApplicationInfo
import java.util.zip.ZipFile
import android.text.TextUtils
import android.R.attr.key
import android.R.attr.entries
import android.content.SharedPreferences
import com.vk.helper.core.helper.SharePreferencesHelper
import java.io.IOException
import java.util.zip.ZipEntry

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2019/4/25
 * @version V1.0.0 < 创建 >
 */
object ChannelUtil {

    private const val KEY_CHANNEL_CACHE = "KEY_CHANNEL_CACHE_${BuildConfig.VERSION_CODE}"

    @JvmStatic
    fun getChannelFrom(context: Context): String {
        val cacheChannel = SharePreferencesHelper.getValue(KEY_CHANNEL_CACHE, "")
        if (cacheChannel.isNotEmpty()) {
            return cacheChannel
        }
        val key = "META-INF/channel"
        val sourceDir = context.applicationInfo.sourceDir
        var ret = ""
        var zipFile: ZipFile? = null
        try {
            zipFile = ZipFile(sourceDir)
            val entries = zipFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val entryName = entry.name
                if (entryName.startsWith(key)) {
                    ret = entryName
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return with(if (!TextUtils.isEmpty(ret)) {
            val split = ret.split("_")
            if (split.size >= 2) {
                ret.substring(split[0].length + 1)
            } else {
                ""
            }
        } else {
            ""
        })
        {
            if (this.isNotEmpty()) {
                SharePreferencesHelper.putValue(KEY_CHANNEL_CACHE, this)
                this
            } else {
                "other"
            }
        }
    }
}