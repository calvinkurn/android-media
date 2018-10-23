package com.tokopedia.discovery.util;

import android.content.Context;

import com.tokopedia.core.analytics.TrackingUtils;

public class MoEngageConstants {

    public String CATEGORY_ID = "";
    public String CATEGORY = "";
    public String PRODUCT_ID = "";
    public String PRODUCT_NAME = "";
    public String SUBCATEGORY = "";
    public String SUBCATEGORY_ID = "";

    public MoEngageConstants(Context context) {

    }

    public void sendCategory(String categoryId, String categoryName) {
        CATEGORY_ID = categoryId;
        CATEGORY = categoryName;
        TrackingUtils.sendMoEngageCategoryEvent(CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public void sendSubCategory(String subCategoryId, String subCategoryName) {
        SUBCATEGORY_ID = subCategoryId;
        CATEGORY = subCategoryName;
        TrackingUtils.sendMoEngageCategoryEvent(CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }

    public void sendProductCategory(String productId, String productName) {
        PRODUCT_ID = productId;
        PRODUCT_NAME = productName;
        TrackingUtils.sendMoEngageCategoryEvent(CATEGORY_ID, CATEGORY, SUBCATEGORY_ID, SUBCATEGORY, PRODUCT_ID, PRODUCT_NAME);
    }
}
