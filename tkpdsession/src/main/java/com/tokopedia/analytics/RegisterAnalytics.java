package com.tokopedia.analytics;


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
    }ChatModule

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


    private RegisterAnalytics() {
    }

    public static RegisterAnalytics initAnalytics() {
        return new RegisterAnalytics();
    }

    public void eventClickBackAddName() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_BACK,
                Category.REGISTER_PAGE,
                "click back on add name page",
                ""
        ));
    }

}
