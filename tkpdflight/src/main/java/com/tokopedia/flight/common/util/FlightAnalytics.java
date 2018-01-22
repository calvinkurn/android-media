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

    public void eventPromotionClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PROMOTION,
                label
        );
    }

    public void eventTripTypeClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_TRIP_TYPE,
                label
        );
    }

    public void eventOriginClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_ORIGIN,
                label
        );
    }

    public void eventDestinationClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_DESTINATION,
                label
        );
    }

    public void eventPassengerClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_PASSENGER,
                label
        );
    }

    public void eventClassClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_CLASS,
                label
        );
    }

    public void eventSearchClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH,
                label
        );
    }

    public void eventSearchProductClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                label
        );
    }

    public void eventSearchDetailClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_DETAIL,
                label
        );
    }

    public void eventDetailPriceTabClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PRICE_TAB,
                label
        );
    }

    public void eventDetailFacilitiesTabClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_FACILITIES_TAB,
                label
        );
    }

    public void eventDetailTabClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_DETAIL_TAB,
                label
        );
    }

    public void eventDetailClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.BOOKING_DETAIL,
                label
        );
    }

    public void eventBookingNextClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.BOOKING_NEXT,
                label
        );
    }

    public void eventReviewNextClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.REVIEW_NEXT,
                label
        );
    }

    public void eventVoucherClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER,
                label
        );
    }

    public void eventVoucherSuccess(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER_SUCCESS,
                label
        );
    }

    public void eventVoucherErrors(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER_ERROR,
                label
        );
    }

    public void eventPurchaseAttempt(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                label
        );
    }

    public void eventAddToCart(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.ADD_TO_CART,
                label
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
        static String CLICK_PROMOTION = "promotion click";
        static String CLICK_TRIP_TYPE = "select trip type";
        static String SELECT_ORIGIN = "select origin";
        static String SELECT_DESTINATION = "select destination";
        static String SELECT_PASSENGER = "select Passenger";
        static String SELECT_CLASS = "select flight class";
        static String CLICK_SEARCH = "click search flight";
        static String CLICK_SEARCH_PRODUCT = "product click";
        static String CLICK_SEARCH_DETAIL = "click see the details";
        static String CLICK_PRICE_TAB = "click price tab";
        static String CLICK_FACILITIES_TAB = "click facilities tab";
        static String CLICK_DETAIL_TAB = "click flights detail tab";
        static String ADD_TO_CART = "add to cart";
        static String BOOKING_DETAIL = "click detail";
        static String BOOKING_NEXT = "click next";
        static String REVIEW_NEXT = "review next";
        static String VOUCHER = "click gunakan voucher code";
        static String VOUCHER_SUCCESS = "voucher success";
        static String VOUCHER_ERROR = "voucher error";
        static String PURCHASE_ATTEMPT = "purchase attempt";

    }

}
