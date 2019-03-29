package com.tokopedia.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * @author by nisie on 1/26/18.
 */

public class RegisterAnalytics {

    public static class Screen {
        public static final String IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT = "RegisterNewNextFragment";
        public static final String IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT = "RegisterPassPhoneFragment";
        public static final String SCREEN_INITIAL_REGISTER = "Register - Initial Page";
        public static final String SCREEN_CREATE_PASSWORD = "Register - Create Password";
        public static final String SCREEN_REGISTER_PAGE = "Register Page";
        public static final String SCREEN_REGISTER_WITH_EMAIL = "Register with Email Page";
        public static final String SCREEN_REGISTER_WITH_PHONE_NUMBER = "Register with Phone Number Page";
        public static final String SCREEN_PHONE_NUMBER_VERIFICATION = "Phone number verification Page";
        public static final String SCREEN_FILL_EMAIL_PAGE = "Fill Email Page";
        public static final String SCREEN_ACCOUNT_ACTIVATION = "Account Activation Page";
    }

    public static class Event {
        public static final String CLICK_LOGIN = "clickLogin";
        public static final String CLICK_CONFIRM = "clickConfirm";
        public static final String CLICK_BACK = "clickBack";
        public static final String CLICK_REGISTER = "clickRegister";
        public static final String CLICK_HOME_PAGE = "clickHomePage";
        public static final String CLICK_USER_PROFILE = "clickUserProfile";
    }

    public static class Category {
        public static final String REGISTER_PAGE = "register page";
        public static final String WELCOME_PAGE = "welcome page";
    }

    public static class Action {
        public static final String INPUT_OTP_PAGE = "input otp page";
        public static final String WELCOME_PAGE = "welcome page";
        public static final String ACTIVATION_PAGE = "activation page";
    }

    private AnalyticTracker tracker;

    private RegisterAnalytics(Context context) {
        this.tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
    }

    public static RegisterAnalytics initAnalytics(Context context) {
        return new RegisterAnalytics(context);
    }

    public void eventClickOnLogin() {
        tracker.sendEventTracking(
                Event.CLICK_LOGIN,
                Category.REGISTER_PAGE,
                "click on masuk",
                ""
        );
    }

    public void eventProceedRegisterWithPhoneNumber() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.REGISTER_PAGE,
                "click on pop up box register (ya, benar)",
                ""
        );
    }

    public void eventCancelRegisterWithPhoneNumber() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "phone number"
        );
    }

    public void eventProceedEmailAlreadyRegistered() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.REGISTER_PAGE,
                "click on pop up box register (ya, masuk)",
                ""
        );
    }

    public void eventCancelEmailAlreadyRegistered() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Category.REGISTER_PAGE,
                "click on pop up box register (ubah)",
                "email"
        );
    }

    public void eventClickBackAddName() {
        tracker.sendEventTracking(
                Event.CLICK_BACK,
                Category.REGISTER_PAGE,
                "click back on add name page",
                ""
        );
    }

    public void eventClickBackRegisterWithEmail() {
        tracker.sendEventTracking(
                Event.CLICK_BACK,
                Category.REGISTER_PAGE,
                "click back (daftar dengan email)",
                ""
        );
    }

    public void eventRegisterWithEmail() {
        tracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Category.REGISTER_PAGE,
                "click on daftar (daftar dengan email)",
                ""
        );
    }

    public void eventContinueFromWelcomePage() {
        tracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                Category.WELCOME_PAGE,
                "click on lanjut",
                ""
        );
    }

    public void eventClickProfileCompletionFromWelcomePage() {
        tracker.sendEventTracking(
                Event.CLICK_USER_PROFILE,
                Category.WELCOME_PAGE,
                "click on lengkapi profil",
                ""
        );
    }

    public void eventClickBackEmailActivation() {
        tracker.sendEventTracking(
                Event.CLICK_BACK,
                Action.ACTIVATION_PAGE,
                "click back button",
                ""
        );
    }

    public void eventClickActivateEmail() {
        tracker.sendEventTracking(
                Event.CLICK_CONFIRM,
                Action.ACTIVATION_PAGE,
                "click on aktivasi",
                ""
        );
    }

    public void eventClickResendActivationEmail() {
        tracker.sendEventTracking(
                Event.CLICK_REGISTER,
                Action.ACTIVATION_PAGE,
                "click on kirim ulang",
                ""
        );
    }

}
