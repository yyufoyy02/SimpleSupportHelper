package com.vk.helper.core.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
import com.vk.helper.core.application

/**
 * 系统网络相关辅助类
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
object NetWorkHelper {

    const val NETWORK_TYPE_UNAVAILABLE = -1
    // private static final int NETWORK_TYPE_MOBILE = -100;
    const val NETWORK_TYPE_WIFI = -101

    const val NETWORK_CLASS_WIFI = -101
    const val NETWORK_CLASS_UNAVAILABLE = -1
    /**
     * Unknown network class.
     */
    const val NETWORK_CLASS_UNKNOWN = 0
    /**
     * Class of broadly defined "2G" networks.
     */
    const val NETWORK_CLASS_2_G = 1
    /**
     * Class of broadly defined "3G" networks.
     */
    const val NETWORK_CLASS_3_G = 2
    /**
     * Class of broadly defined "4G" networks.
     */
    const val NETWORK_CLASS_4_G = 3

    // 适配低版本手机
    /**
     * Network type is unknown
     */
    const val NETWORK_TYPE_UNKNOWN = 0
    /**
     * Current network is GPRS
     */
    const val NETWORK_TYPE_GPRS = 1
    /**
     * Current network is EDGE
     */
    const val NETWORK_TYPE_EDGE = 2
    /**
     * Current network is UMTS
     */
    const val NETWORK_TYPE_UMTS = 3
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    const val NETWORK_TYPE_CDMA = 4
    /**
     * Current network is EVDO revision 0
     */
    const val NETWORK_TYPE_EVDO_0 = 5
    /**
     * Current network is EVDO revision A
     */
    const val NETWORK_TYPE_EVDO_A = 6
    /**
     * Current network is 1xRTT
     */
    const val NETWORK_TYPE_1xRTT = 7
    /**
     * Current network is HSDPA
     */
    const val NETWORK_TYPE_HSDPA = 8
    /**
     * Current network is HSUPA
     */
    const val NETWORK_TYPE_HSUPA = 9
    /**
     * Current network is HSPA
     */
    const val NETWORK_TYPE_HSPA = 10
    /**
     * Current network is iDen
     */
    const val NETWORK_TYPE_IDEN = 11
    /**
     * Current network is EVDO revision B
     */
    const val NETWORK_TYPE_EVDO_B = 12
    /**
     * Current network is LTE
     */
    const val NETWORK_TYPE_LTE = 13
    /**
     * Current network is eHRPD
     */
    const val NETWORK_TYPE_EHRPD = 14
    /**
     * Current network is HSPA+
     */
    const val NETWORK_TYPE_HSPAP = 15

    private val connectivityManager by lazy {
        application()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val telephonyManager by lazy {
        application()
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    @JvmStatic
    fun isConnected(): Boolean {
        return try {
            val info = connectivityManager.activeNetworkInfo
            return null != info && info.isConnected && info.state == NetworkInfo.State.CONNECTED
        } catch (e: NullPointerException) {
            false
        }
    }

    /**
     * 判断是否是wifi连接
     */
    @JvmStatic
    fun isWifi(context: Context): Boolean {
        return try {
            if (connectivityManager.activeNetworkInfo != null) connectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI else false
        } catch (e: NullPointerException) {
            false
        }
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    @JvmStatic
    fun getCurrentNetworkType(mContext: Context): String {
        val networkClass = getNetworkClass(mContext)
        var type = "Unknow"
        when (networkClass) {
            NETWORK_CLASS_UNAVAILABLE -> type = "NoConnection"
            NETWORK_CLASS_WIFI -> type = "Wi-Fi"
            NETWORK_CLASS_2_G -> type = "2G"
            NETWORK_CLASS_3_G -> type = "3G"
            NETWORK_CLASS_4_G -> type = "4G"
            NETWORK_CLASS_UNKNOWN -> type = "Unknow"
        }
        return type
    }

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        return "NoConnection" != getCurrentNetworkType(context)
    }

    private fun getNetworkClass(mContext: Context): Int {
        var networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN
        try {
            val network = connectivityManager.activeNetworkInfo
            if (network != null && network.isAvailable
                    && network.isConnected) {
                val type = network.type
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    val telephonyManager = mContext.getSystemService(
                            Context.TELEPHONY_SERVICE) as TelephonyManager
                    networkType = telephonyManager.networkType
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return getNetworkClassByType(networkType)
    }

    private fun getNetworkClassByType(networkType: Int): Int {
        return when (networkType) {
            NETWORK_TYPE_UNAVAILABLE -> NETWORK_CLASS_UNAVAILABLE
            NETWORK_TYPE_WIFI -> NETWORK_CLASS_WIFI
            NETWORK_TYPE_GPRS, NETWORK_TYPE_EDGE, NETWORK_TYPE_CDMA, NETWORK_TYPE_1xRTT, NETWORK_TYPE_IDEN -> NETWORK_CLASS_2_G
            NETWORK_TYPE_UMTS, NETWORK_TYPE_EVDO_0, NETWORK_TYPE_EVDO_A, NETWORK_TYPE_HSDPA, NETWORK_TYPE_HSUPA, NETWORK_TYPE_HSPA, NETWORK_TYPE_EVDO_B, NETWORK_TYPE_EHRPD, NETWORK_TYPE_HSPAP -> NETWORK_CLASS_3_G
            NETWORK_TYPE_LTE -> NETWORK_CLASS_4_G
            else -> NETWORK_CLASS_UNKNOWN
        }
    }

    /**
     * 打开网络设置界面
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun openSetting(activity: Activity) {
        var intent = Intent()
        /**
         * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
         */
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
        } else {
            val component = ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings")
            intent.component = component
            intent.action = "android.intent.action.VIEW"
        }
        activity.startActivity(intent)
    }

    /**
     * 获取运营商
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getProvider(mContext: Context): String {
        var provider = "未知"
        try {
            val IMSI: String? = telephonyManager.subscriberId
            Log.v("tag", "getProvider.IMSI:$IMSI")
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                                .simState) {
                    val operator: String? = telephonyManager.simOperator
                    Log.v("tag", "getProvider.operator:$operator")
                    if (operator != null) {
                        if (operator == "46000"
                                || operator == "46002"
                                || operator == "46007") {
                            provider = "中国移动"
                        } else if (operator == "46001") {
                            provider = "中国联通"
                        } else if (operator == "46003") {
                            provider = "中国电信"
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    provider = "中国移动"
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通"
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return provider
    }

    /**
     * 检查sim卡状态
     */
    @JvmStatic
    fun checkSimState(mContext: Context): Boolean {
        return try {
            return telephonyManager.simState != TelephonyManager.SIM_STATE_ABSENT &&
                    telephonyManager.simState != TelephonyManager.SIM_STATE_UNKNOWN
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 获取imei
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getImei(mContext: Context): String {
        var imei = "000000000000000"
        try {
            imei = telephonyManager.deviceId
            if (imei == null) {
                imei = "000000000000000"
            }
        } catch (e: Exception) {
        }
        return imei
    }

    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getPhoneImsi(mContext: Context): String {
        try {
            return telephonyManager.subscriberId
        } catch (e: Exception) {
        }
        return ""
    }
}