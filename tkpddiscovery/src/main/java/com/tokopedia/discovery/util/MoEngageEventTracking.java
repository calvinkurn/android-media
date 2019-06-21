package com.tokopedia.discovery.util;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.track.TrackApp;

import java.util.Map;

public class MoEngageEventTracking {

    public static String CATEGORY_ID = "";
    public static String CATEGORY = "";
    public static String PRODUCT_ID = "";
    public static String PRODUCT_NAME = "";
    public static String SUBCATEGORY = "";
    public static String SUBCATEGORY_ID = "";

    public static void sendCategory(String categoryId, String categoryName) {
        CATEGORY_ID = categoryId;
        CATEGORY = categoryName;
        SUBCATEGORY = "";
        SUBCATEGORY_ID ="";
        PRODUCT_NAME = "";
        PRODUCT_ID = "";
        sendMoEngageCategoryEvent(MainApplication.getAppContext(), CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public static void sendSubCategory(String subCategoryId, String subCategoryName) {
        SUBCATEGORY_ID = subCategoryId;
        SUBCATEGORY = subCategoryName;
        PRODUCT_ID = "";
        PRODUCT_NAME = "";
        sendMoEngageCategoryEvent(MainApplication.getAppContext(), CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public static void sendProductCategory(String productId, String productName) {
        PRODUCT_ID = productId;
        PRODUCT_NAME = productName;
        sendMoEngageCategoryEvent(MainApplication.getAppContext(), CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public static void sendMoEngageCategoryEvent(Context context, String categoryId, String categoryName, String subCategoryId, String subCategoryName, String productId, String productName) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.CATEGORY, categoryName,
                AppEventTracking.MOENGAGE.CATEGORY_ID, categoryId,
                AppEventTracking.MOENGAGE.PRODUCT_GROUP_NAME, productName,
                AppEventTracking.MOENGAGE.PRODUCT_GROUP_ID, productId,
                AppEventTracking.MOENGAGE.SUBCATEGORY_ID, subCategoryId,
                AppEventTracking.MOENGAGE.SUBCATEGORY, subCategoryName
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.CAT_SCREEN_OPEN);
    }
}
