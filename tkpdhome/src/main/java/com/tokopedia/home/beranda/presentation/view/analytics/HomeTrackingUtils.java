package com.tokopedia.home.beranda.presentation.view.analytics;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.explore.domain.model.LayoutRows;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashwanityagi on 19/03/18.
 */

public class HomeTrackingUtils {

    public static void homeSlidingBannerClick(BannerSlidesModel bannerSlidesModel) {
        Map<String, Object> bundle = new HashMap<>();

        bundle.put(FirebaseParams.Home.BANNER_CREATIVE, bannerSlidesModel.getCreativeName());
        bundle.put(FirebaseParams.Home.BANNER_NAME, bannerSlidesModel.getTitle());
        bundle.put(FirebaseParams.Home.BANNER_POSITION, bannerSlidesModel.getSlideIndex());
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, bannerSlidesModel.getApplink());
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_CLICK, bundle);
    }

    public static void homeViewAllPromotions(String landingScreenName) {
        Map<String, Object> bundle = new HashMap<>();

        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreenName);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_VIEW_ALL_CLICK, bundle);
    }


    public static void homeUsedCaseImpression(String iconName, int iconPosition) {
        Map<String, Object> bundle = new HashMap<>();

        bundle.put(FirebaseParams.Home.ICON_NAME, iconName);
        bundle.put(FirebaseParams.Home.ICON_POSITION, iconPosition);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_USED_CASE_IMPRESSION, bundle);
    }

    public static void homeUsedCaseClick(String iconName, int iconPosition, String landingScreenName) {
        Map<String, Object> bundle = new HashMap<>();

        bundle.put(FirebaseParams.Home.ICON_NAME, iconName);
        bundle.put(FirebaseParams.Home.ICON_POSITION, iconPosition);
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreenName);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_USED_CASE_CLICK, bundle);
    }


    public static void homeSprintSaleImpression(int position, DynamicHomeChannel.Channels channel) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.PRODUCT_ID, channel.getId());
        bundle.put(FirebaseParams.Home.PRODUCT_NAME, channel.getName());
        bundle.put(FirebaseParams.Home.PRODUCT_LIST_NAME, channel.getPromoName());
        bundle.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_IMPRESSION + "_" + position, bundle);
    }


    public static void homeSprintSaleViewAll(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_VIEW_ALL_CLICK, bundle);
    }


    public static void homeSprintSaleClick(int position, DynamicHomeChannel.Channels channel, String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.PRODUCT_ID, channel.getId());
        bundle.put(FirebaseParams.Home.PRODUCT_NAME, channel.getName());
        bundle.put(FirebaseParams.Home.PRODUCT_LIST_NAME, channel.getPromoName());
        bundle.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_CLICK + "_" + position, bundle);
    }


    public static void homeDiscoveryWidgetImpression(int position, DynamicHomeChannel.Channels channel) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.PRODUCT_ID, channel.getId());
        bundle.put(FirebaseParams.Home.PRODUCT_NAME, channel.getName());
        bundle.put(FirebaseParams.Home.PRODUCT_LIST_NAME, channel.getPromoName());
        bundle.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_IMPRESSION, bundle);
    }


    public static void homeDiscoveryWidgetClick(int position, DynamicHomeChannel.Channels channel, String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.PRODUCT_ID, channel.getId());
        bundle.put(FirebaseParams.Home.PRODUCT_NAME, channel.getName());
        bundle.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        bundle.put(FirebaseParams.Home.PRODUCT_LIST_NAME, "Discovery Widget");
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_CLICK, bundle);
    }


    public static void homeDiscoveryWidgetViewAll(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_VIEW_ALL_CLICK, bundle);
    }


    public static void homeNavTopHomeClick(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_HOME_CLICKED, bundle);
    }


    public static void homeNavTopHotlistClick(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_HOTLIST_CLICKED, bundle);
    }


    public static void homeNavTopFeedClick(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_FEED_CLICKED, bundle);
    }


    public static void homeNavTopFavoriteClick(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOP_NAV_FAVORIT_CLICKED, bundle);
    }

    public static void cartIconClicked(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.CART_ICON_CLICKED, bundle);
    }

    public static void homeSearchIconClicked(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SEARCH_ICON_CLICKED, bundle);
    }

//    public static void homeRecommedationImpression(String landingScreen) {
//        Map<String,Object> bundle=new HashMap<>();
//        bundle.put(FirebaseParams.Home.PRODUCT_ID, channel.getId());
//        bundle.put(FirebaseParams.Home.PRODUCT_NAME, channel.getName());
//        bundle.put(FirebaseParams.Home.PRODUCT_LIST_NAME, channel.getPromoName());
//        bundle.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
//        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
//        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_SEARCH_ICON_CLICKED ,bundle);
//    }

    public static void homepageRecommedationClicked(LayoutRows rowModel) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.PRODUCT_ID, rowModel.getId());
        bundle.put(FirebaseParams.Home.PRODUCT_NAME, rowModel.getName());
        bundle.put(FirebaseParams.Home.PRODUCT_LIST_NAME, "Home page recommendation");
        bundle.put(FirebaseParams.Home.PRODUCT_CATEGORY, rowModel.getType());
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, rowModel.getApplinks());
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_RECOMMEDATION_CLICKED, bundle);
    }

    public static void homepageHamburgerClick(){
        Map<String, Object> bundle = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_HAMBURGER_CLICK, bundle);
    }


    public static void sendEventToAnalytics(String eventName, Map<String, Object> data){
        TrackAnalytics.sendEvent(eventName,data, MainApplication.getAppContext());
    }

}
