package com.tokopedia.home.beranda.presentation.view.analytics;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationProductViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashwanityagi on 19/03/18.
 */

public class HomeTrackingUtils {

    public static void homeSlidingBannerImpression(BannerSlidesModel bannerSlidesModel, int position) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.BANNER_CREATIVE, bannerSlidesModel.getCreativeName());
        map.put(FirebaseParams.Home.BANNER_NAME, bannerSlidesModel.getTitle());
        map.put(FirebaseParams.Home.BANNER_LIST_NAME, "sliding baaner");
        map.put(FirebaseParams.Home.BANNER_POSITION, position);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_IMPRESSION, map);
    }

    public static void homeSlidingBannerClick(BannerSlidesModel bannerSlidesModel, int position) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.BANNER_CREATIVE, bannerSlidesModel.getCreativeName());
        map.put(FirebaseParams.Home.BANNER_NAME, bannerSlidesModel.getTitle());
        map.put(FirebaseParams.Home.BANNER_LIST_NAME, "sliding baaner");
        map.put(FirebaseParams.Home.BANNER_POSITION, position);
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, bannerSlidesModel.getApplink());
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_CLICK, map);
    }

    public static void homeViewAllPromotions(String landingScreenName) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreenName);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_VIEW_ALL_CLICK, map);
    }


    public static void homeUsedCaseImpression(List<LayoutSections> sectionList) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < sectionList.size(); i++) {
            map.put(FirebaseParams.Home.ICON_NAME + "_" + i + 1, sectionList.get(i).getTitle());
            map.put(FirebaseParams.Home.ICON_POSITION + "_" + i + 1, i + 1);
        }

        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_USED_CASE_IMPRESSION, map);
    }

    public static void homeUsedCaseClick(String iconName, int iconPosition, String landingScreenName) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.ICON_NAME, iconName);
        map.put(FirebaseParams.Home.ICON_POSITION, iconPosition);
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreenName);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_USED_CASE_CLICK, map);
    }


    public static void homeSprintSaleImpression(DynamicHomeChannel.Grid[] grids , String category) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < grids.length; i++) {
            map.put(FirebaseParams.Home.PRODUCT_ID,grids[i].getId());
            map.put(FirebaseParams.Home.PRODUCT_NAME, grids[i].getLabel());
            map.put(FirebaseParams.Home.PRODUCT_CATEGORY, category);
        }
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_IMPRESSION, map);
    }


    public static void homeSprintSaleViewAll(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_VIEW_ALL_CLICK, map);
    }


    public static void homeSprintSaleClick(int position, DynamicHomeChannel.Channels channel, DynamicHomeChannel.Grid grid, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.PRODUCT_ID, grid.getId());
        map.put(FirebaseParams.Home.PRODUCT_NAME, grid.getLabel());
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_CLICK + "_" + position, map);
    }


    public static void homeDiscoveryWidgetImpression(int position, DynamicHomeChannel.Channels channel) {
        Map<String, Object> map = new HashMap<>();
        DynamicHomeChannel.Grid[] grids = channel.getGrids();
        for (int i = 0; i < grids.length; i++) {
            map.put(FirebaseParams.Home.PRODUCT_ID + "_" + i, grids[i].getId());
            map.put(FirebaseParams.Home.PRODUCT_NAME+ "_" + i, grids[i].getName());
        }

        map.put(FirebaseParams.Home.PRODUCT_LIST_NAME, channel.getPromoName());
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_IMPRESSION + "_" + position, map);
    }


    public static void homeDiscoveryWidgetClick(int position, DynamicHomeChannel.Grid grid, String landingScreen, String category) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.PRODUCT_ID, grid.getId());
        map.put(FirebaseParams.Home.PRODUCT_NAME, grid.getLabel());
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, category);
        map.put(FirebaseParams.Home.PRODUCT_LIST_NAME, "Discovery Widget");
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_CLICK + "_" + position, map);
    }


    public static void homeDiscoveryWidgetViewAll(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_VIEW_ALL_CLICK, map);
    }


    public static void homeNavTopHomeClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_HOME_CLICKED, map);
    }


    public static void homeNavTopHotlistClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_HOTLIST_CLICKED, map);
    }


    public static void homeNavTopFeedClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_FEED_CLICKED, map);
    }


    public static void homeNavTopFavoriteClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_FAVORIT_CLICKED, map);
    }

    public static void cartIconClicked(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.CART_ICON_CLICKED, map);
    }

    public static void homeSearchIconClicked(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SEARCH_ICON_CLICKED, map);
    }

    public static void homepageRecommedationClicked(InspirationProductViewModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.PRODUCT_ID, model.getProductId());
        map.put(FirebaseParams.Home.PRODUCT_NAME, model.getName());
        map.put(FirebaseParams.Home.PRODUCT_LIST_NAME, "Home page recommendation");
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, model.getRecommedationType());
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, "Product details screen");
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_RECOMMEDATION_CLICKED, map);
    }

    public static void homepageHamburgerClick() {
        Map<String, Object> map = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_HAMBURGER_CLICK, map);
    }


    public static void sendEventToAnalytics(String eventName, Map<String, Object> data) {
        TrackAnalytics.sendEvent(eventName, data, MainApplication.getAppContext());
    }

}
