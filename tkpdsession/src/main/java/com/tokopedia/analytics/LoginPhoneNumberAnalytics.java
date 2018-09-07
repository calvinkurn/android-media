package com.tokopedia.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;

/**
 * @author by nisie on 1/5/18.
 */

public class LoginPhoneNumberAnalytics {

    public static class Event {
        static final String EVENT_CLICK_LOGIN = "clickLogin";
    }

    public static class Category {
        static final String LOGIN_WITH_PHONE = "login with phone";
        static final String PHONE_VERIFICATION = "phone verification";
    }

    public static class Action {

        static final String CLICK_ON_NEXT = "click on selanjutnya";
        static final String CLICK_ON_VERIFICATION = "click on verifikasi";
        static final String CLICK_ON_RESEND_VERIFICATION = "click on kirim ulang";
        static final String LOGIN_SUCCESS = "login success";
        public static final String CHANGE_METHOD = "change method";

    }

    public static class Label {

        static final String SMS = "sms";
        static final String PHONE = "phone";
        static final String TOKOCASH = "Tokocash";
    }

    public static EventTracking getLoginWithPhoneTracking() {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.CLICK_ON_NEXT,
                ""
        );
    }

    public static EventTracking getVerifyTracking(int type) {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_VERIFICATION,
                getTypeLabel(type)
        );
    }

    private static String getTypeLabel(int type) {
        switch (type) {
            case VerificationActivity.TYPE_SMS:
                return Label.SMS;
            case VerificationActivity.TYPE_PHONE_CALL:
                return Label.PHONE;
            default:
                return "";
        }
    }

    public static EventTracking getResendVerificationTracking(int type) {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_RESEND_VERIFICATION,
                getTypeLabel(type)
        );
    }

    public static EventTracking getSuccessLoginTracking() {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.LOGIN_SUCCESS,
                Label.TOKOCASH
        );
    }

    public static EventTracking getChooseVerificationMethodTracking(int type) {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CHANGE_METHOD,
                getTypeLabel(type)
        );
    }

    public static class Screen {
        public static final String SCREEN_LOGIN_PHONE_NUMBER = "Login by Phone Number";
        public static final String SCREEN_CHOOSE_TOKOCASH_ACCOUNT = "choose account";
        public static final String SCREEN_NOT_CONNECTED_TO_TOKOCASH = "Login Tokocash - Not Connected";
    }
}
