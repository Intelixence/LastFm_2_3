package com.intelixence.lastfm1.Utils;

public class CustomTime {

    public static String minutes_seconds(int seconds){
        long long_minutes = seconds/60;
        int int_seconds = seconds%60;
        String string_minutes = String.valueOf(long_minutes);
        if (long_minutes < 10){
            string_minutes = "0"+long_minutes;
        }
        String string_seconds = String.valueOf(int_seconds);
        if (int_seconds < 10){
            string_seconds = "0"+int_seconds;
        }
        return string_minutes+":"+string_seconds;
    }
}
