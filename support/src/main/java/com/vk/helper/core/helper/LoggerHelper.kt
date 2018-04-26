package com.vk.helper.core.helper

import android.util.Log
import java.util.*

/**
 * Logger类
 *
 * @author Viking
 * @date 2018/4/24
 * @version V1.0.0 < 创建 >
 */
object LoggerHelper {

    private var logFlag = true
    private val sLoggerTable = Hashtable<String, Logger>()
    private const val tag = "[LogUtil]"
    private var logLevel = Log.VERBOSE

    @JvmStatic
    fun setDebugMode(logFlags: Boolean, logLevel: Int = Log.VERBOSE) {
        logFlag = logFlags
        LoggerHelper.logLevel = logLevel
    }

    @JvmStatic
    fun log(className: String = ""): Logger {
        var classLogger: Logger? = sLoggerTable[className]
        if (classLogger == null) {
            classLogger = Logger(className)
            sLoggerTable[className] = classLogger
        }
        return classLogger
    }

    class Logger(private val mClassName: String) {

        /**
         * Get The Current Function Name
         */
        private val functionName: String?
            get() {
                val sts = Thread.currentThread().stackTrace ?: return null
                for (st in sts) {
                    if (st.isNativeMethod) {
                        continue
                    }
                    if (st.className == Thread::class.java.name) {
                        continue
                    }
                    if (st.className == this.javaClass.name) {
                        continue
                    }
                    return (mClassName + "[ " + Thread.currentThread().name + ": "
                            + st.fileName + ":" + st.lineNumber + " "
                            + st.methodName + " ]")
                }
                return null
            }

        /**
         * The Log Level:i
         *
         * @param str
         */
        fun i(str: Any?) {
            if (logFlag && logLevel <= Log.INFO) {
                val name = functionName
                if (name != null) {
                    Log.i(tag, "$name - $str")
                } else {
                    Log.i(tag, str.toString())
                }
            }
        }

        /**
         * The Log Level:d
         *
         * @param str
         */
        fun d(str: Any?) {
            if (logFlag && logLevel <= Log.DEBUG) {
                val name = functionName
                if (name != null) {
                    Log.d(tag, "$name - $str")
                } else {
                    Log.d(tag, str.toString())
                }
            }
        }

        /**
         * The Log Level:d
         *
         * @param str
         */
        fun d(tag: String, str: Any?) {
            if (logFlag && logLevel <= Log.DEBUG) {
                val name = functionName
                if (name != null) {
                    Log.d(tag, "$name - $str")
                } else {
                    Log.d(tag, str.toString())
                }
            }
        }

        /**
         * The Log Level:V
         *
         * @param str
         */
        fun v(str: Any?) {
            if (logFlag && logLevel <= Log.VERBOSE) {
                val name = functionName
                if (name != null) {
                    Log.v(tag, "$name - $str")
                } else {
                    Log.v(tag, str.toString())
                }
            }
        }

        /**
         * The Log Level:w
         *
         * @param str
         */
        fun w(str: Any?) {
            if (logFlag && logLevel <= Log.WARN) {
                val name = functionName
                if (name != null) {
                    Log.w(tag, "$name - $str")
                } else {
                    Log.w(tag, str.toString())
                }
            }
        }

        /**
         * The Log Level:e
         *
         * @param str
         */
        fun e(str: Any?) {
            if (logFlag && logLevel <= Log.ERROR) {
                val name = functionName
                if (name != null) {
                    Log.e(tag, "$name - $str")
                } else {
                    Log.e(tag, str.toString())
                }
            }
        }

        /**
         * The Log Level:e
         *
         * @param ex
         */
        fun e(ex: Exception) {
            if (logFlag && logLevel <= Log.ERROR) {
                Log.e(tag, "error", ex)
            }
        }

        fun e(e: Throwable) {
            if (logFlag && logLevel <= Log.ERROR) {
                Log.e(tag, "error", e)
            }
        }

        /**
         * The Log Level:e
         *
         * @param log
         * @param tr
         */
        fun e(log: String, tr: Throwable) {
            if (logFlag) {
                val line = functionName
                Log.e(tag, "{Thread:" + Thread.currentThread().name + "}"
                        + "[" + mClassName + line + ":] " + log + "\n", tr)
            }
        }
    }
}