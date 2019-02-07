package com.tokopedia.discovery.util;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.discovery.autocomplete.viewmodel.BaseItemAutoCompleteSearch;

import java.util.Map;

public class AutoCompleteTracking {


    public static final String CLICK_TOP_NAV = "clickTopNav";
    public static final String TOP_NAV = "top nav";
    public static final String CLICK_POPULAR_SEARCH = "click - popular search";
    public static final String CLICK_RECENT_SEARCH = "click - recent search";
    public static final String CLICK_DIGITAL_PRODUCT_SUGGESTION = "click - digital product suggestion";
    public static final String CLICK_CATEGORY_SUGGESTION = "click - category suggestion";
    public static final String CLICK_SEARCH = "click - search";
    public static final String EVENT_CLICK_SEARCH = "clickSearch";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String ECOMMERCE = "ecommerce";
    public static final String PRODUCT_CLICK = "productClick";
    public static final String CLICK_RECENT_VIEW_PRODUCT = "click - recent view product";
    public static final String CLICK = "click";
    public static final String ACTION_FIELD = "actionField";
    public static final String LIST = "list";
    public static final String RECENT_VIEW_ACTION_FIELD = "/search - recentview - product";
    public static final String PRODUCTS = "products";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_BRAND = "brand";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_VARIANT = "variant";
    public static final String PRODUCT_POSITION = "position";
    public static final String NONE_OTHER = "none / other";
    private static final String LABEL_RECENT_VIEW_CLICK = "po: %s - applink: %s";
    private static final String LABEL_HOTLIST_CLICK = "keyword: %s - hotlist: %s - hotlist id: %s - po: %s - applink: %s";
    public static final String ACTION_CLICK_HOTLIST_SUGGESTION = "click - hotlist suggestion";

    public static void eventClickPopularSearch(Context context, String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH,
                TOP_NAV,
                CLICK_POPULAR_SEARCH,
                label
        );
    }

    public static void eventClickRecentSearch(Context context, String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH,
                TOP_NAV,
                CLICK_RECENT_SEARCH,
                label
        );
    }

    public static void eventClickAutoCompleteSearch(Context context, String label, String tabName) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH_RESULT,
                TOP_NAV,
                String.format("click - product autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickShopSearch(Context context,
                                            String label,
                                            String tabName) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH_RESULT,
                TOP_NAV,
                String.format("click - shop autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickInCategory(Context context,
                                            String label,
                                            String tabName) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH_RESULT,
                TOP_NAV,
                String.format("click - category autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickInHotlist(Context context,
                                           String keyword,
                                           String hotlistName,
                                           String id,
                                           int position,
                                           String applink) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        clearEventTracking(context);

        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_TOP_NAV,
                TOP_NAV,
                ACTION_CLICK_HOTLIST_SUGGESTION,
                String.format(
                        LABEL_HOTLIST_CLICK,
                        keyword,
                        hotlistName,
                        id,
                        String.valueOf(position),
                        applink
                )
        );
    }

    public static void eventClickCategory(Context context,
                                          String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH_RESULT,
                TOP_NAV,
                CLICK_CATEGORY_SUGGESTION,
                label
        );
    }

    public static void eventClickDigital(Context context, String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH_RESULT,
                TOP_NAV,
                CLICK_DIGITAL_PRODUCT_SUGGESTION,
                label
        );
    }

    public static void eventClickSubmit(Context context, String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH,
                TOP_NAV,
                CLICK_SEARCH,
                label
        );
    }

    public static void eventClickRecentView(Context context,
                                            String position,
                                            BaseItemAutoCompleteSearch data) {
        Map<String, Object> productData = convertSearchItemToProductData(data, position);
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf(EVENT, PRODUCT_CLICK,
                        EVENT_CATEGORY, TOP_NAV,
                        EVENT_ACTION, CLICK_RECENT_VIEW_PRODUCT,
                        EVENT_LABEL, String.
                                format(LABEL_RECENT_VIEW_CLICK,
                                position,
                                data.getApplink()),
                        ECOMMERCE, DataLayer.mapOf(
                                CLICK,
                                        DataLayer.mapOf(
                                                ACTION_FIELD, DataLayer.mapOf(LIST, RECENT_VIEW_ACTION_FIELD),
                                                PRODUCTS, DataLayer.listOf(
                                                        productData
                                                )
                                )
                        )
                )
        );
    }

    private static Map<String, Object> convertSearchItemToProductData(BaseItemAutoCompleteSearch data,
                                                                      String position) {
        return DataLayer.mapOf(
                PRODUCT_NAME, data.getKeyword(),
                PRODUCT_ID, data.getProductId(),
                PRODUCT_PRICE, data.getProductPrice(),
                PRODUCT_BRAND, NONE_OTHER,
                PRODUCT_CATEGORY, NONE_OTHER,
                PRODUCT_VARIANT, NONE_OTHER,
                PRODUCT_POSITION, position
        );
    }

    private static void clearEventTracking(Context context) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(DataLayer.mapOf("event", null,
                "eventCategory", null,
                "eventAction", null,
                "eventLabel", null
        ));
    }
}
