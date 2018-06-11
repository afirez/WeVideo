package com.afirez.wevideo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by afirez on 18/6/7.
 */

public class DateUtils {

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        return str;
    }

}
