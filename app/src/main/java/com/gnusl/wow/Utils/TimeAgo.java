package com.gnusl.wow.Utils;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        TimeZone.setDefault(TimeZone.getTimeZone("UTC+2"));
        long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(now);
        String date1 = DateFormat.format("yyyy-MMM-dd HH:mm:ss", cal).toString();
        if (time > now || time <= 0) {
            return null;
        }
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just Now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < 96 * HOUR_MILLIS) {
            return diff / DAY_MILLIS +  " days ago";
        } else {
            Date date = new Date(time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return simpleDateFormat.format(date);
        }
    }
}