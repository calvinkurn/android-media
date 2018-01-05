package com.tokopedia.analytics;

import android.util.Log;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * @author by nisie on 1/5/18.
 */

public class LoginPhoneNumberAnalytics {

    public static class Event {

        static final String EVENT_CLICK_LOGIN = "eventClickLogin";
    }

    public static class Category {

        static final String LOGIN_WITH_PHONE = "login with phone";
        static final String PHONE_VERIFICATION = "phone verification";
    }

    public static class Action {

        static final String CLICK_ON_NEXT = "click on selanjutnya";
        static final String CLICK_ON_VERIFICATION = "click on verifikasi";
        public static final String CLICK_ON_RESEND_VERIFICATION = "click on kirim ulang";
    }


    public static EventTracking getLoginWithPhoneTracking() {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.LOGIN_WITH_PHONE,
                Action.CLICK_ON_NEXT,
                ""
        );
    }

    public static EventTracking getVerifyTracking() {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_VERIFICATION,
                ""
        );
    }

    public static EventTracking getResendVerificationTracking() {
        return new EventTracking(
                Event.EVENT_CLICK_LOGIN,
                Category.PHONE_VERIFICATION,
                Action.CLICK_ON_RESEND_VERIFICATION,
                ""
        );
    }
}
