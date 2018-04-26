package com.viking.vikingdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 跟App相关的辅助类
 *
 * @author Administrator
 */
public class XAppUtil {

    public static final String THIRD_PACKAGE_WECHAT = "com.tencent.mm";
    private static Context context;

    private XAppUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public static void init(Context contexts) {
        context = contexts.getApplicationContext();
    }

    /**
     * 获取唯一android ID
     */
    public static String getDeviceAndroidId(Context mContext) {
        if (mContext == null) {
            return null;
        }
        return Secure.getString(mContext.getContentResolver(),
                Secure.ANDROID_ID);
    }

    /**
     * 获取唯一DeviceToken
     */
    public static String getDeviceToken() {
        return getDeviceAndroidId(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static double getScreenSizeOfDevice2(Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

    /**
     * 获取唯一DeviceToken
     */
    // public static String getDeviceToken(Context mContext) {
    //     TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    //     String deviceId = tm.getDeviceId();
    //     if (deviceId == null || deviceId.length() == 0) {
    //         WifiManager manager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    //         if (manager != null) {
    //             deviceId = manager.getConnectionInfo().getMacAddress();
    //         }
    //     }
    //     return deviceId;
    // }

    /**
     * 获取设备品牌
     */
    public static String getBrand(){
        return Build.BRAND;
    }

    /**
     * 获取设备型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * OS版本号
     */
    public static String getRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 屏幕分辨率
     */
    public static String getResolution(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels + "x" + dm.widthPixels;
    }

    /**
     * 获取总运存大小
     */
    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
//            for (String num : arrayOfString) {
//                Log.i(str2, num + "\t");
//            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
//        System.out.println("总运存--->>>"+initial_memory/(1024*1024));
        return initial_memory / (1024 * 1024);
    }

    /**
     * 获取唯一android ID
     */
    public static String getDeviceAndroidId() {
        return getDeviceAndroidId(context);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppName() {

        return getAppName(context);
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号(内部识别号)
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;

        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 获取版本号(内部识别号)
     *
     * @return
     */
    public static int getVersionCode() {
        return getVersionCode(context);
    }

    public static String getVersionName() {

        return getVersionName(context);
    }

    public static void setMultiLanguage(Context context, Locale locale) {
        if (locale == null || context == null) {
            return;
        }
        Configuration config = context.getResources().getConfiguration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }

    public static Locale getMultiLanguage(Context context) {
        if (context == null) {
            return null;
        }
        return context.getResources().getConfiguration().locale;

    }

    public static Locale getMultiLanguage() {
        return getMultiLanguage(context);

    }

    public static void setMultiLanguage(Locale locale) {
        setMultiLanguage(context, locale);

    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return new PackageInfo();
    }

    /**
     * 判断手机上是否安装了目标APP
     *
     * @param context
     * @return
     */
    public static boolean isPackageAvailable(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos == null) {
            return false;
        }

        for (int i = 0; i < packageInfos.size(); i++) {
            String pn = packageInfos.get(i).packageName;
            if (pn.equals(packageName)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isWChatAvailable(Context context) {
        return isPackageAvailable(context, THIRD_PACKAGE_WECHAT);
    }

    public static void startWeChat(Context context) {
        if (isWChatAvailable(context)) {

            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");// 报名该有activity
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);

            context.startActivity(intent);
        } else {
            // XToastUtil.showToast(context.getString(R.string.no_install_wchat));
        }
    }



}