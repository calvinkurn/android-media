package com.tokopedia.flight.common.util;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import javax.inject.Inject;

/**
 * @author by alvarisi on 1/19/18.
 */

public class FlightAnalytics {
    private AnalyticTracker analyticTracker;
    private String GENERIC_EVENT = "genericFlightEvent";
    private String GENERIC_CATEGORY = "digital - flight";

    @Inject
    public FlightAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickTransactions(String screenName) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_TRANSACTIONS,
                screenName
        );
    }

    public static final class Screen {
        public static String HOMEPAGE = "Homepage";
        public static String SEARCH = "Search";
        public static String SEARCH_RETURN = "Search Return";
        public static String REVIEW = "Review";
        public static String BOOKING = "Booking";
    }

    private static class Category {
        static String CLICK_TRANSACTIONS = "click transaction list";
    }
}
