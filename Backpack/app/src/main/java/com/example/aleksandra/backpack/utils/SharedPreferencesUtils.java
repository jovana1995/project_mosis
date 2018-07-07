package com.example.aleksandra.backpack.utils;

import android.content.SharedPreferences;


/**
 * Created by Aleksandra on 20/01/2018.
 */



@SuppressWarnings("unused")
public class SharedPreferencesUtils {

    private static final String PREF_NAME = "backpack";

    private static SharedPreferencesUtils instance = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesUtils getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesUtils();
        }
        return instance;
    }

    // Constructor
    private SharedPreferencesUtils() {
        prefs = com.example.aleksandra.backpack.BackpackApplication.getAppContext().getSharedPreferences(PREF_NAME, 0);
        editor = prefs.edit();
    }

    public void clearPreferences() {
        prefs.edit().clear().commit();
    }

    public void cacheId(int id) {
        try {
            putPrefInt(Constants.MY_ID, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putPrefString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPrefString(String key) {
        return prefs.getString(key, null);
    }

    public void deletePrefString(String key) {
        editor.remove(key).commit();
    }

    public void putPrefInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getPrefInt(String key) {
        return prefs.getInt(key, -1);
    }

    public void putPrefLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getPrefLong(String key) {
        return prefs.getLong(key, -1);
    }

    public void putPrefBoolean(String key, boolean b) {
        editor.putBoolean(key, b);
        editor.commit();
    }

    public boolean getPrefBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public void deletePref(String key) {
        editor.remove(key);
        editor.apply();
    }
}
