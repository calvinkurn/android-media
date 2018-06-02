package com.tokopedia.discovery.util;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppEventTracking;

public class AutoCompleteTracking {


    public static final String CLICK_TOP_NAV = "clickTopNav";
    public static final String TOP_NAV = "top nav";
    public static final String CLICK_POPULAR_SEARCH = "click - popular search";
    public static final String CLICK_RECENT_SEARCH = "click - recent search";
    public static final String CLICK_DIGITAL_PRODUCT_SUGGESTION = "click - digital product suggestion";
    public static final String CLICK_CATEGORY_SUGGESTION = "click - category suggestion";

    public static void eventClickPopularSearch(Context context, String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_TOP_NAV,
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
                CLICK_TOP_NAV,
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
                CLICK_TOP_NAV,
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
                CLICK_TOP_NAV,
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
                CLICK_TOP_NAV,
                TOP_NAV,
                String.format("click - category autocomplete - tab: %s", tabName),
                label
        );
    }

    public static void eventClickCategory(Context context,
                                          String label) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_TOP_NAV,
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
                CLICK_TOP_NAV,
                TOP_NAV,
                CLICK_DIGITAL_PRODUCT_SUGGESTION,
                label
        );
    }
}
