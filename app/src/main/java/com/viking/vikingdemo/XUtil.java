package com.viking.vikingdemo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 应用常用的静态工具方法集合
 *
 * @author vikingliang
 */
public class XUtil {
    private static long lastClickTime;
    private static Context context;

    private XUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void init(Context contexts) {
        context = contexts.getApplicationContext();
    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static Application getAppContext() {
        return (Application) (context.getApplicationContext());
    }

    public static boolean isPad() {
        return isPad(context);
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground() {
        return isApplicationBroughtToBackground(context);
    }

    /**
     * 控件是否被快速点击
     */
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static <T, K> boolean isEmpty(Map<T, K> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isEmpty(Set<T> set) {
        return set == null || set.isEmpty();
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return list != null && !list.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Array List 等列表子类获取 index 对应的值时，若 index 大于等于列表的 size 会报错，所以设计一个工具类获取安全值，否则返回 null
     *
     * @param index 列表对应的下标
     * @param <T> 定义列表内 数据的 类型为泛型
     * @return 列表内存在的时候返回 <T> 泛型，列表内不存在 index 的时候返回 null 不报错
     */
    @Nullable
    public static <T> T listSafeGet(List<T> dataList, int index) {
        if (dataList == null) {
            return null;
        }
        if (index < 0 || index >= dataList.size()) {
            return null;
        }
        return dataList.get(index);
    }

    public static <T> T replaceIfNull(T target, T replaceValue) {
        if (target == null) {
            return replaceValue;
        }
        return target;
    }

    public static String replaceIfEmpty(String target, String replaceValue) {
        if (target == null || target.isEmpty()) {
            return replaceValue;
        }
        return target;
    }

    public static <T> List<T> replaceIfEmpty(List<T> target, List<T> replaceValue) {
        if (target == null || target.isEmpty()) {
            return replaceValue;
        }
        return target;
    }

    public static String removeCityLastChar(String city) {
        if ("市".equals(city.substring(city.length() - 1))) {
            city = city.substring(0, city.length() - 1);
        }
        return city;
    }

    /**
     * 判断手机是否装了微信
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void launchWechat(Context context) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    public static Bundle getApplicationMetaData(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                return appInfo.metaData;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Bundle();
    }

    public static String getApplicationMetaDataByKey(String key) {
        Bundle bundle = getApplicationMetaData(context);
        return bundle.getString(key);
    }

    /**
     * 获取当前系统的最上层 activity 栈的第一个 activity 的组件对象
     *
     * @return 组件对象
     */
    @Nullable
    public static ComponentName getTopComponent() {
        ComponentName result = null;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // 仍然可以返回自己 app 的 task
            List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(1);
            if (list == null || list.size() <= 0) {
                return null;
            }
            ActivityManager.RunningTaskInfo activityTask = list.get(0);
            if (activityTask != null) {
                return activityTask.topActivity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前系统的最上层 activity 栈的第一个 activity 的组件名
     *
     * @return true 表示是前台显示
     */
    public static boolean isForegroundRunning() {
        ComponentName component = getTopComponent();
        if (component == null) {
            return false;
        }
        return component.getPackageName().equals(context.getPackageName());
    }

    /**
     * 返回当前 app 是否为前台显示
     *
     * @return 获取的组件对象的名称
     */
    @Nullable
    public static String getTopComponentClassName() {
        ComponentName component = getTopComponent();
        if (component != null) {
            return component.getClassName();
        }
        return null;
    }
}
