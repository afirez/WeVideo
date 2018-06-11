package com.afirez.wevideo.common.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Settings;

/**
 * Created by afirez on 18/6/7.
 */

public class SystemUtils {

    //获取亮度
    public static int getBrightness(Context context) {
        return Settings.System.getInt(context.getContentResolver(),"screen_brightness", -1);
    }
    //设置亮度
    public static void setBrightness(Context context, int value) {
        Settings.System.putInt(context.getContentResolver(), "screen_brightness", value);
    }
    //获取亮度的sharedPreferences文件
    public static int getDefaultBrightness(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("shared_preferences_light", -1);
    }

    public static boolean setDefaultBrightness(Context context, int value) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("shared_preferences_light", value).commit();
    }
}
