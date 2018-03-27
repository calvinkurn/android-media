package com.tokopedia.analytics;

import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.core.app.MainApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashwanityagi on 26/03/18.
 */

public class SessionAnalyticsEventsHelper {

    public static void loginPageEnterEmail(String emailId) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.EMAIL_ID, emailId);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_ENTER_EMAIL, map);
    }

    public static void loginPageEnterPasword() {
        Map<String, Object> map = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_ENTER_PASSWORD, map);
    }

    public static void loginPageClickLogin() {
        Map<String, Object> map = new HashMap<>();
        // map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN, map);
    }

    public static void loginPageClickLoginFacebook( ) {
        Map<String, Object> map = new HashMap<>();
        //map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_FACEBOOK, map);
    }
    public static void loginPageClickLoginGoogle() {
        Map<String, Object> map = new HashMap<>();
        //  map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_GOOGLE, map);
    }
    public static void loginPageClickLoginPhone() {
        Map<String, Object> map = new HashMap<>();
        // map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_PHONE, map);
    }
    public static void loginPageClickLoginYahoo(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_LOGIN_YAHOO, map);
    }
    public static void loginPageClickForgotPassword(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_FORGOT_PASSWORD, map);
    }
    public static void loginPageClickSignup(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.LOGIN_PAGE_CLICK_SIGNUP, map);
    }

    public static void sendEventToAnalytics(String eventName, Map<String, Object> data){
        TrackAnalytics.sendEvent(eventName,data, MainApplication.getAppContext());
    }
}
