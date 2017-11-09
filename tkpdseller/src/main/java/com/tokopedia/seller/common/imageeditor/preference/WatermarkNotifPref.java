package com.tokopedia.seller.common.imageeditor.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.core.app.MainApplication;

public class WatermarkNotifPref {
    private static final String PREF_NAME = "WatermarkPref";

    private static final String HAS_SHOWN_WATERMARK = "w_mark";

    private static SharedPreferences instance;

    private static SharedPreferences getPrefInstance(){
        if (instance == null) {
            instance = MainApplication.getAppContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public static void setHasShown(boolean hasShown){
        SharedPreferences.Editor editor = getPrefInstance().edit().putBoolean(HAS_SHOWN_WATERMARK, hasShown);
        editor.commit();
    }

    public static boolean hasShown(){
        return getPrefInstance().getBoolean(HAS_SHOWN_WATERMARK, false);
    }
}
