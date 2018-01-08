package com.tokopedia.session.login.loginemail;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * @author by nisie on 1/8/18.
 */

public class LoginAnalytics {

    public static class Event {
        public static final String LOGIN_SUCCESS = "loginSuccess";
        public static final String LOGIN_CLICK = "clickLogin";
        public static final String REGISTER_LOGIN = "registerLogin";
    }

    public static class Category {
        public static final String LOGIN = "Login";
    }

    public static class Action {
        public static final String LOGIN_SUCCESS = "Login Success";
        public static final String CLICK = "Click";
        public static final String REGISTER = "Register";
    }

    public static class Label {
        public static final String EMAIL = "Email";
        public static final String PHONE_NUMBER = "Phone Number";
        public static final String GPLUS = "Google Plus";
        public static final String FACEBOOK = "Facebook";
        public static final String REGISTER = "Register";
    }

    public static EventTracking getEventSuccessLoginEmail() {
        return new EventTracking(
                Event.LOGIN_SUCCESS,
                Category.LOGIN,
                Action.LOGIN_SUCCESS,
                Label.EMAIL
        );
    }

    public static EventTracking getEventClickLoginWebview(String name) {
        return new EventTracking(
                Event.LOGIN_CLICK,
                Category.LOGIN,
                Action.CLICK,
                name
        );
    }

    public static EventTracking getEventClickLoginPhoneNumber() {
        return new EventTracking(
                Event.LOGIN_CLICK,
                Category.LOGIN,
                Action.CLICK,
                Label.PHONE_NUMBER
        );
    }

    public static EventTracking getEventClickLoginGoogle() {
        return new EventTracking(
                Event.LOGIN_CLICK,
                Category.LOGIN,
                Action.CLICK,
                Label.GPLUS
        );
    }

    public static EventTracking getEventClickLoginFacebook() {
        return new EventTracking(
                Event.LOGIN_CLICK,
                Category.LOGIN,
                Action.CLICK,
                Label.FACEBOOK
        );
    }

    public static EventTracking goToRegisterFromLogin() {
        return new EventTracking(
                Event.REGISTER_LOGIN,
                Category.LOGIN,
                Action.REGISTER,
                Label.REGISTER
        );
    }

    public static EventTracking getEventSuccessLoginSosmed(String loginMethod) {
        return new EventTracking(
                Event.REGISTER_LOGIN,
                Category.LOGIN,
                Action.REGISTER,
                loginMethod
        );
    }
}
