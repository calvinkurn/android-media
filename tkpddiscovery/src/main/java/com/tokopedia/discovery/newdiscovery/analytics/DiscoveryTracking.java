package com.tokopedia.discovery.newdiscovery.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.discovery.newdiscovery.constant.DiscoveryEventTracking;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTrackingConstant.CLICK_CARI;
import static com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTrackingConstant.LONG_CLICK;
import static com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTrackingConstant.LONG_PRESS;
import static com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTrackingConstant.PRODUCT_SEARCH;
import static com.tokopedia.discovery.newdiscovery.analytics.DiscoveryTrackingConstant.USER_ID;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class DiscoveryTracking {

    public static final String EVENT_CATEGORY_EMPTY_SEARCH = "empty search";
    public static final String EVENT_CLICK_SEARCH_RESULT = "clickSearchResult";
    public static final String EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru";

    private UserSessionInterface userSessionInterface;

    @Inject
    public DiscoveryTracking(UserSessionInterface userSessionInterface) {
        this.userSessionInterface = userSessionInterface;
    }

    public static void eventSearchResultShare(Context context, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DiscoveryEventTracking.Event.SEARCH_RESULT,
                DiscoveryEventTracking.Category.SEARCH_SHARE,
                DiscoveryEventTracking.Action.CLICK_BAR + screenName,
                ""
        ));
    }

    public static void eventSearchResultChangeGrid(Context context, String gridName, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DiscoveryEventTracking.Event.SEARCH_RESULT,
                DiscoveryEventTracking.Category.GRID_MENU,
                DiscoveryEventTracking.Action.CLICK_CHANGE_GRID + gridName,
                screenName
        ));
    }

    public static void eventSearchResultCatalogClick(Context context, String keyword, String catalogName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                DiscoveryEventTracking.Event.SEARCH_RESULT,
                DiscoveryEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                DiscoveryEventTracking.Action.CLICK_CATALOG,
                keyword + " - " + catalogName
        ));
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

    public static void eventSearchNoResult(Context context,
                                           String keyword, String screenName,
                                           Map<String, String> selectedFilter) {

        eventSearchNoResult(keyword, screenName, selectedFilter, "", "");
    }

    private static void eventSearchNoResult(String keyword, String screenName,
                                           Map<String, String> selectedFilter,
                                           String alternativeKeyword,
                                           String resultCode) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DiscoveryEventTracking.Event.EVENT_VIEW_SEARCH_RESULT,
                DiscoveryEventTracking.Category.EVENT_TOP_NAV,
                String.format(DiscoveryEventTracking.Action.NO_SEARCH_RESULT_WITH_TAB, screenName),
                String.format("keyword: %s - type: %s - alternative: %s - param: %s",
                        keyword,
                        !TextUtils.isEmpty(resultCode) ? resultCode : "none/other",
                        !TextUtils.isEmpty(alternativeKeyword) ? alternativeKeyword : "none/other",
                        generateFilterEventLabel(selectedFilter))
        );
    }

    public void eventSearchShortcut() {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(DiscoveryTrackingConstant.EVENT, LONG_CLICK);
        eventTracking.put(DiscoveryTrackingConstant.EVENT_CATEGORY, LONG_PRESS);
        eventTracking.put(DiscoveryTrackingConstant.EVENT_ACTION, CLICK_CARI);
        eventTracking.put(DiscoveryTrackingConstant.EVENT_LABEL, PRODUCT_SEARCH);
        eventTracking.put(USER_ID, userSessionInterface.isLoggedIn() ? userSessionInterface.getUserId() : "0");

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventTracking);
    }

    public static void eventUserClickNewSearchOnEmptySearch(Context context, String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_EMPTY_SEARCH,
                EVENT_ACTION_CLICK_NEW_SEARCH,
                String.format("tab: %s", screenName)
        );
    }
}
