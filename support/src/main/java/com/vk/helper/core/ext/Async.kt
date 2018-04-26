package com.vk.helper.core.ext

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * UI线程
 */
fun Context.runOnUiThread(f: Context.() -> Unit) {
    if (ContextHelper.mainThread == Thread.currentThread()) f() else ContextHelper.handler.post { f() }
}

/**
 * Fragment UI线程
 */
inline fun Fragment.runOnUiThread(crossinline f: () -> Unit) {
    activity?.runOnUiThread { f() }
}

/**
 *  自定义Context
 */
class AsyncContext<T>(val weakRef: WeakReference<T>)

/**
 * UI线程
 * 当前context已经会回收，继续调用
 */
fun <T> AsyncContext<T>.onComplete(f: (T?) -> Unit) {
    val ref = weakRef.get()
    if (ContextHelper.mainThread == Thread.currentThread()) {
        f(ref)
    } else {
        ContextHelper.handler.post { f(ref) }
    }
}

/**
 * UI线程
 * 当前context已经会回收，则不调用
 */
fun <T> AsyncContext<T>.uiThread(f: (T) -> Unit): Boolean {
    val ref = weakRef.get() ?: return false
    if (ContextHelper.mainThread == Thread.currentThread()) {
        f(ref)
    } else {
        ContextHelper.handler.post { f(ref) }
    }
    return true
}

/**
 * Activity使用的UI线程
 * 如果activity已经finish则不会调用
 */
fun <T : Activity> AsyncContext<T>.activityUiThread(f: (T) -> Unit): Boolean {
    val activity = weakRef.get() ?: return false
    if (activity.isFinishing) return false
    activity.runOnUiThread { f(activity) }
    return true
}

fun <T : Activity> AsyncContext<T>.activityUiThreadWithContext(f: Context.(T) -> Unit): Boolean {
    val activity = weakRef.get() ?: return false
    if (activity.isFinishing) return false
    activity.runOnUiThread { activity.f(activity) }
    return true
}

fun <T : Fragment> AsyncContext<T>.fragmentUiThread(f: (T) -> Unit): Boolean {
    val fragment = weakRef.get() ?: return false
    if (fragment.isDetached) return false
    val activity = fragment.activity ?: return false
    activity.runOnUiThread { f(fragment) }
    return true
}

fun <T : Fragment> AsyncContext<T>.fragmentUiThreadWithContext(f: Context.(T) -> Unit): Boolean {
    val fragment = weakRef.get() ?: return false
    if (fragment.isDetached) return false
    val activity = fragment.activity ?: return false
    activity.runOnUiThread { activity.f(fragment) }
    return true
}

/**
 * 发起异步线程
 * 使用默认程序池
 *
 * @param exceptionHandler 错误回调
 * @param task 异步线程
 */
fun <T> T.doAsync(
        exceptionHandler: ((Throwable) -> Unit)? = null,
        task: AsyncContext<T>.() -> Unit
): Future<Unit> {
    val context = AsyncContext(WeakReference(this))
    return BackgroundExecutor.submit {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr) ?: Unit
        }
    }
}

/**
 * 发起异步线程
 *
 * @param exceptionHandler 错误回调
 * @param executorService 自定义线程池
 * @param task 异步线程
 */
fun <T> T.doAsync(
        exceptionHandler: ((Throwable) -> Unit)? = null,
        executorService: ExecutorService,
        task: AsyncContext<T>.() -> Unit
): Future<Unit> {
    val context = AsyncContext(WeakReference(this))
    return executorService.submit<Unit> {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
        }
    }
}

/**
 * 有返回值的异步线程
 *
 * @param exceptionHandler 错误回调
 * @param task 异步线程
 */
fun <T, R> T.doAsyncResult(
        exceptionHandler: ((Throwable) -> Unit)? = null,
        task: AsyncContext<T>.() -> R
): Future<R> {
    val context = AsyncContext(WeakReference(this))
    return BackgroundExecutor.submit {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
            throw thr
        }
    }
}

/**
 * 有返回值的异步线程
 *
 * @param exceptionHandler 错误回调
 * @param executorService 自定义线程池
 * @param task 异步线程
 */
fun <T, R> T.doAsyncResult(
        exceptionHandler: ((Throwable) -> Unit)? = null,
        executorService: ExecutorService,
        task: AsyncContext<T>.() -> R
): Future<R> {
    val context = AsyncContext(WeakReference(this))
    return executorService.submit<R> {
        try {
            context.task()
        } catch (thr: Throwable) {
            exceptionHandler?.invoke(thr)
            throw thr
        }
    }
}

/**
 * 默认使用的线程对象
 */
internal object BackgroundExecutor {
    //初始化线程池，默认容量CPU核数*2
    private var executor: ExecutorService =
            Executors.newScheduledThreadPool(2 * Runtime.getRuntime().availableProcessors())

    fun <T> submit(task: () -> T): Future<T> {
        return executor.submit(task)
    }

}

/**
 * 线程辅助工具
 */
private object ContextHelper {
    val handler = Handler(Looper.getMainLooper())
    val mainThread: Thread = Looper.getMainLooper().thread
}