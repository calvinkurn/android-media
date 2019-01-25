package com.tokopedia.discovery.newdiscovery.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.newdiscovery.analytics.SearchConstant.*;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking {

    private static final String ACTION_FIELD = "/searchproduct - p$1 - product";
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String ECOMMERCE = "ecommerce";
    public static final String EVENT_EMPTY = "";
    public static final String EVENT_CATEGORY_SEARCH_RESULT_PROFILE = "search result profile";
    public static final String EVENT_ACTION_CLICK_PROFILE_RESULT = "click - profile result";
    public static final String PROMO_CLICK = "promoClick";
    public static final String PROMOTIONS = "promotions";
    public static final String VALUE_FOLLOW = "follow";
    public static final String VALUE_UNFOLLOW = "unfollow";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT_ACTION_CLICK_FOLLOW_ACTION_PROFILE = "click - %s profile";
    public static final String EVENT_LABEL_CLICK_FOLLOW_ACTION_PROFILE = "keyword: %s - profile: %s - profile id: %s - po: %s";
    public static final String PROMO_VIEW = "promoView";
    public static final String EVENT_ACTION_IMPORESSION_PROFILE = "imporession - profile";
    public static String imageClick = "/imagesearch - p%s";

    private AnalyticTracker tracker;
    private UserSessionInterface userSessionInterface;

    @Inject
    public SearchTracking(Context context, UserSessionInterface userSessionInterface) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        this.tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        this.userSessionInterface = userSessionInterface;
    }

    public static String getActionFieldString(int pageNumber) {
        return ACTION_FIELD.replace("$1", Integer.toString(pageNumber));
    }

    public static void trackEventClickSearchResultProduct(Context context,
                                                          Object item,
                                                          int pageNumber,
                                                          String eventLabel,
                                                          Map<String, String> selectedFilter,
                                                          Map<String, String> selectedSort) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", eventLabel,
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", getActionFieldString(pageNumber)),
                                        "products", DataLayer.listOf(item)
                                )
                        ),
                        "searchFilter", concatFilterAndSortEventLabel(
                                generateFilterEventLabel(selectedFilter),
                                generateSortEventLabel(selectedSort)
                        )
                )
        );
    }

    public static void trackEventClickImageSearchResultProduct(Context context, Object item, int position) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", String.format(imageClick, position)),
                                        "products", DataLayer.listOf(item)
                                )
                        )
                )
        );

        tracker.sendEventTracking(
                AppEventTracking.Event.PRODUCT_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH_RESULT,
                AppEventTracking.Action.CLICK_PRODUCT,
                ""
        );
    }

    public static void eventImpressionSearchResultProduct(Context context, List<Object> list, String eventLabel) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "search result",
                        "eventAction", "impression - product",
                        "eventLabel", eventLabel,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    public static void eventImpressionImageSearchResultProduct(Context context, List<Object> list) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", AppEventTracking.Category.IMAGE_SEARCH_RESULT,
                        "eventAction", "impression - product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    public static void eventClickGuidedSearch(Context context, String previousKey, String page, String nextKey) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                "clickSearchResult",
                "search result",
                "click - guided search",
                String.format("%s - %s - %s", previousKey, nextKey, page)
        );
    }

    public static void eventClickRelatedSearch(Context context, String currentKeyword, String relatedKeyword) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                "",
                "search result",
                "click - related keyword",
                String.format("%s - %s", currentKeyword, relatedKeyword)
        );
    }

    public static void eventImpressionGuidedSearch(Context context, String currentKey, String page) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                "viewSearchResult",
                "search result",
                "impression - guided search",
                String.format("%s - %s", currentKey, page)
        );
    }

    public static void eventSearchResultShopItemClick(Context context, String keyword, String shopName,
                                                      int page, int position) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP,
                keyword + " - " + shopName + " - " + Integer.toString(page) + " - " + Integer.toString(position)
        );
    }

    public static void eventSearchResultShare(Context context, String screenName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_SHARE,
                AppEventTracking.Action.CLICK_BAR + screenName,
                ""
        );
    }

    public static void eventSearchResultChangeGrid(Context context, String gridName, String screenName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.GRID_MENU,
                AppEventTracking.Action.CLICK_CHANGE_GRID + gridName,
                screenName
        );
    }

    public static void eventSearchResultFavoriteShopClick(Context context, String keyword, String shopName,
                                                          int page, int position) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.FAVORITE_SHOP_CLICK,
                keyword + " - " + shopName + " - " + Integer.toString(page) + " - " + Integer.toString(position)
        );
    }

    public static void eventSearchResultCatalogClick(Context context, String keyword, String catalogName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.CLICK_CATALOG,
                keyword + " - " + catalogName
        );
    }

    public static void eventSearchResultTabClick(Context context, String tabTitle) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_TAB,
                AppEventTracking.Action.CLICK_TAB,
                tabTitle
        );
    }

    public static void eventSearchResultFilter(Context context, String screenName, Map<String, String> selectedFilter) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_PRODUCT,
                AppEventTracking.Action.FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        );
    }

    public static void eventSearchResultCloseBottomSheetFilter(Context context,
                                                               String screenName,
                                                               Map<String, String> selectedFilter) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_PRODUCT,
                AppEventTracking.Action.APPLY_FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        );
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        if (selectedFilter == null) {
            return "";
        }
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }

    private static String generateSortEventLabel(Map<String, String> selectedSort) {
        if (selectedSort == null) {
            return "";
        }
        List<String> sortList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedSort.entrySet()) {
            sortList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", sortList);
    }

    private static String concatFilterAndSortEventLabel(String filterEventLabel, String sortEventLabel) {
        if (TextUtils.isEmpty(filterEventLabel)) {
            return sortEventLabel;
        } else {
            return filterEventLabel + "&" + sortEventLabel;
        }
    }

    public static void eventSearchResultFilterJourney(Context context,
                                                      String filterName,
                                                      String filterValue,
                                                      boolean isInsideDetail, boolean isActive) {

        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                AppEventTracking.Action.CLICK.toLowerCase() + " - "
                        + filterName + ": " + filterValue + " - "
                        + (isInsideDetail ? "inside lihat semua" : "outside lihat semua"),
                Boolean.toString(isActive)
        );
    }

    public static void eventSearchResultApplyFilterDetail(Context context, String filterName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                "click simpan on lihat semua " + filterName,
                ""
        );
    }

    public static void eventSearchResultBackFromFilterDetail(Context context, String filterName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                "click back on lihat semua " + filterName,
                ""
        );
    }

    public static void eventSearchResultNavigateToFilterDetail(Context context, String filterName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_JOURNEY,
                "click lihat semua " + filterName,
                ""
        );
    }

    public static void eventSearchResultOpenFilterPageProduct(Context context) {
        eventSearchResultOpenFilterPage(context, "product");
    }

    public static void eventSearchResultOpenFilterPageCatalog(Context context) {
        eventSearchResultOpenFilterPage(context,"catalog");
    }

    public static void eventSearchResultOpenFilterPageShop(Context context) {
        eventSearchResultOpenFilterPage(context,"shop");
    }

    private static void eventSearchResultOpenFilterPage(Context context, String tabName) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER.toLowerCase() + " " + tabName,
                AppEventTracking.Action.CLICK_FILTER,
                ""
        );
    }

    public static void eventSearchNoResult(Context context,
                                           String keyword, String screenName,
                                           Map<String, String> selectedFilter) {

        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEventTracking(
                AppEventTracking.Event.NO_RESULT,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.NO_SEARCH_RESULT,
                "keyword: " + keyword + " - tab: " + screenName + " - param: " + generateFilterEventLabel(selectedFilter)
        );
    }

    public void eventSearchShortcut() {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SearchConstant.EVENT, LONG_CLICK);
        eventTracking.put(SearchConstant.EVENT_CATEGORY, LONG_PRESS);
        eventTracking.put(SearchConstant.EVENT_ACTION, CLICK_CARI);
        eventTracking.put(SearchConstant.EVENT_LABEL, PRODUCT_SEARCH);
        eventTracking.put(USER_ID, userSessionInterface.isLoggedIn() ? userSessionInterface.getUserId() : "0");

        tracker.sendEventTracking(eventTracking);
    }

    public static void eventUserClickProfileResultInTabProfile(Context context,
                                                               List<Object> profileData,
                                                               String keyword) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_SEARCH_RESULT,
                        EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT_PROFILE,
                        EVENT_ACTION, EVENT_ACTION_CLICK_PROFILE_RESULT,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_CLICK, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(
                                                profileData.toArray(new Object[profileData.size()])
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickFollowActionProfileResultProfileTab(Context context,
                                                                     String keyword,
                                                                     boolean isFollow,
                                                                     String profileName,
                                                                     String profileId,
                                                                     int position) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        String foKey = "";
        if (isFollow) {
            foKey = VALUE_FOLLOW;
        } else {
            foKey = VALUE_UNFOLLOW;
        }

        tracker.sendEventTracking(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_SEARCH_RESULT_PROFILE,
                String.format(
                        EVENT_ACTION_CLICK_FOLLOW_ACTION_PROFILE,
                        foKey
                ),
                String.format(
                        EVENT_LABEL_CLICK_FOLLOW_ACTION_PROFILE,
                        keyword,
                        profileName,
                        profileId,
                        position
                )
        );
    }

    public static void eventUserImpressionProfileResultInTabProfile(Context context,
                                                               List<Object> profileData,
                                                               String keyword) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf(
                        EVENT, PROMO_VIEW,
                        EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT_PROFILE,
                        EVENT_ACTION, EVENT_ACTION_IMPORESSION_PROFILE,
                        EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                                PROMO_VIEW, DataLayer.mapOf(
                                        PROMOTIONS, DataLayer.listOf(
                                                profileData.toArray(new Object[profileData.size()])
                                        )
                                )
                        )
                )
        );
    }
}
