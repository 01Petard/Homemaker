package com.wjx.homemaker.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtils {

    private static final String FILE_NAME = "wjx";
    private static final String MODE_NAME = "welcome";
    private static final String LOG_NAME = "login";

    public static boolean isFirstStart(Context context) {

        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean(MODE_NAME, true);
    }

    public static void putIsFirstStart(Context context, boolean isFirst) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putBoolean(MODE_NAME, isFirst);
        editor.commit();
    }

    public static void putName(Context context, String username) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putString("username", username);
        editor.commit();
    }

    public static String getName(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("username", null);
    }

    public static void putPass(Context context, String password) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putString("password", password);
        editor.commit();
    }

    public static String getPass(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString("password", null);
    }

    public static void putClick(Context context, Boolean isClick) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putBoolean("isClick", isClick);
        editor.commit();
    }

    public static boolean getClick(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean("isClick", false);
    }




}
