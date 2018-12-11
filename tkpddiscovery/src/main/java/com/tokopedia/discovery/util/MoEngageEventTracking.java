package com.tokopedia.discovery.util;

import android.content.Context;

import com.tokopedia.core.analytics.TrackingUtils;

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
        TrackingUtils.sendMoEngageCategoryEvent(CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public static void sendSubCategory(String subCategoryId, String subCategoryName) {
        SUBCATEGORY_ID = subCategoryId;
        SUBCATEGORY = subCategoryName;
        PRODUCT_ID = "";
        PRODUCT_NAME = "";
        TrackingUtils.sendMoEngageCategoryEvent(CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public static void sendProductCategory(String productId, String productName) {
        PRODUCT_ID = productId;
        PRODUCT_NAME = productName;
        TrackingUtils.sendMoEngageCategoryEvent(CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }
}
