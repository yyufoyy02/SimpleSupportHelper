package com.vk.helper.core.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import android.util.Log
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * 描述这个类的作用
 *
 * @author Viking
 * @date 2018/4/25
 * @version V1.0.0 < 创建 >
 */
object SecurityHelper {

    private const val TAG = "XSecurityUtil"
    const val MD5 = "MD5"
    const val SHA1 = "SHA1"
    const val SHA256 = "SHA256"

    /**
     * 展示了如何用Java代码获取签名
     */
    @SuppressLint("PackageManagerGetSignatures")
    @JvmStatic
    fun getSignature(context: Context): Signature? {
        try {
            // 下面几行代码展示如何任意获取Context对象，在jni中也可以使用这种方式
            //            Class<?> activityThreadClz = Class.forName("android.app.ActivityThread");
            //            Method currentApplication = activityThreadClz.getMethod("currentApplication");
            //            Application application = (Application) currentApplication.invoke(null);
            //            PackageManager pm = application.getPackageManager();
            //            PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);

            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val signatures = pi.signatures
            return signatures[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun encodeSignature(sig: Signature, type: String): String {
        val hexBytes = sig.toByteArray()
        return encodeSignature(hexBytes, type)
    }

    @JvmStatic
    fun encodeSignature(hexBytes: ByteArray, type: String): String {
        var fingerprint = "error!"
        try {
            val digest = MessageDigest.getInstance(type)
            if (digest != null) {
                val digestBytes = digest.digest(hexBytes)
                val sb = StringBuilder()
                for (digestByte in digestBytes) {
                    sb.append(Integer.toHexString((digestByte and 0xFF.toByte() or 0x100.toByte()).toInt()).substring(1, 3))
                }
                fingerprint = sb.toString()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return fingerprint
    }

    /**
     * HMAC-SHA1加密
     * @param secret
     * @param baseString
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    @JvmStatic
    fun hmacSHA1Signature(secret: String, baseString: String): ByteArray? {
        if (secret.isEmpty()) {
            throw IOException("secret can not be empty")
        }
        if (baseString.isEmpty()) {
            return null
        }
        val mac = Mac.getInstance("HmacSHA1")
        val keySpec = SecretKeySpec(secret.toByteArray(charset("UTF-8")), mac.algorithm)
        mac.init(keySpec)
        return mac.doFinal(baseString.toByteArray(charset("UTF-8")))
    }

    @Throws(UnsupportedEncodingException::class)
    @JvmStatic
    fun newStringByBase64(bytes: ByteArray?): String? {
        return if (bytes == null || bytes.isEmpty()) {
            null
        } else String(Base64.encode(bytes, Base64.DEFAULT))
    }

    @JvmStatic
    fun encodeHmacSHA1(data: String, key: String): String {
        try {
            val mac = Mac.getInstance("HmacSHA1")
            val secret = SecretKeySpec(key.toByteArray(charset("UTF-8")), mac.algorithm)
            mac.init(secret)
            val sb = StringBuilder()
            for (digestByte in mac.doFinal(data.toByteArray(charset("UTF-8")))) {
                sb.append(Integer.toHexString((digestByte and 0xFF.toByte() or 0x100.toByte()).toInt()).substring(1, 3))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "Hash algorithm SHA-1 is not supported", e)
        } catch (e: UnsupportedEncodingException) {
            Log.e(TAG, "Encoding UTF-8 is not supported", e)
        } catch (e: InvalidKeyException) {
            Log.e(TAG, "Invalid key", e)
        }

        return ""
    }
}