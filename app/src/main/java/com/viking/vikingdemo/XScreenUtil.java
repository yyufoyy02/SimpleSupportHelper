package com.viking.vikingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * 获得屏幕相关的辅助类
 *
 * @author Administrator
 */
public class XScreenUtil {
    private XScreenUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Rect windowFrameRect = new Rect();
    static Context context;

    public static void init(Context contexts) {
        context = contexts.getApplicationContext();
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenWidth() {
        return getScreenWidth(context);
    }

    public static int getMAXWidth() {
        int w = getScreenWidth();
        return w > 1080 ? 1080 : w;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenHeight() {

        return getScreenHeight(context);
    }

    public static int getWindowWidth(Activity activity) {
        if (activity == null) {
            return 0;
        }
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(windowFrameRect);
        return windowFrameRect.width();
    }

    public static int getWindowHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(windowFrameRect);
        return windowFrameRect.height();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static int getStatusHeight() {

        return getStatusHeight(context);
    }

    public static int getNavigationBarHeight() {
        if (isNavigationBarShow()) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            return resources.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static boolean isNavigationBarShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    public static int getWidthByScreenProportion(float percentage, int minWidth) {
        int result = (int) percentage * getScreenWidth();
        return result > minWidth ? result : minWidth;
    }

    public static int getHeightByScreenProportion(float percentage, int minHeight){
        int result = (int) percentage * getScreenHeight();
        return result > minHeight ? result : minHeight;
    }

}
