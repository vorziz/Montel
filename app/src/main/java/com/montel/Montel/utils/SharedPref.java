package com.montel.Montel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPreferences mSharedPref;
    public static final String NOTIFICATION= "IS_SELECT";

    private SharedPref()
    {

    }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

//    public static String read(String key, String defValue) {
//        return mSharedPref.getString(key, defValue);
//    }
//
//    public static void write(String key, String value) {
//        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
//        prefsEditor.putString(key, value);
//        prefsEditor.commit();
//    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }


}
