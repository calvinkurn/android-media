package com.tokopedia.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * @author by alvinatin on 06/06/18.
 */

public class ChangePhoneNumberAnalytics {

    public static class Event {
        static final String CLICK_PROFILE = "clickUserProfile";
    }

    public static class Category {
        static final String WARNING_PAGE = "warning page";
        static final String CHANGE_PHONE_NUMBER_PAGE = "ubah no telepon page";
    }

    public static class Action {
        static final String VIEW_WARNING_MESSAGE = "view on warning message";
        static final String CLICK_ON_SALDO = "click on saldo";
        static final String CLICK_ON_NEXT = "click on lanjutkan";
        static final String CLICK_ON_INFORMATION = "click on information icon";
    }

    public static class Label {
        static final String WARNING_1 = "warning 1";
        static final String WARNING_2 = "warning 2";
        static final String WARNING_3 = "warning 3";
        static final String EMPTY = "";
    }

    public static EventTracking getEventViewWarningMessageTokocash() {
        return new EventTracking(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.VIEW_WARNING_MESSAGE,
                Label.WARNING_1
        );
    }

    public static EventTracking getEventViewWarningMessageSaldo() {
        return new EventTracking(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.VIEW_WARNING_MESSAGE,
                Label.WARNING_2
        );
    }

    public static EventTracking getEventViewWarningMessageBoth() {
        return new EventTracking(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.VIEW_WARNING_MESSAGE,
                Label.WARNING_3
        );
    }

    public static EventTracking getEventWarningPageClickOnWithdraw() {
        return new EventTracking(
                Event.CLICK_PROFILE,
                Category.WARNING_PAGE,
                Action.CLICK_ON_SALDO,
                Label.EMPTY
        );
    }

    public static EventTracking getEventChangePhoneNumberClickOnNext() {
        return new EventTracking(
                Event.CLICK_PROFILE,
                Category.CHANGE_PHONE_NUMBER_PAGE,
                Action.CLICK_ON_NEXT,
                Label.EMPTY
        );
    }

    public static EventTracking getEventChangePhoneNumberClickOnInfo() {
        return new EventTracking(
                Event.CLICK_PROFILE,
                Category.CHANGE_PHONE_NUMBER_PAGE,
                Action.CLICK_ON_INFORMATION,
                Label.EMPTY
        );
    }
}
