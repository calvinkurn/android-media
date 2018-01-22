package com.tokopedia.flight.common.util;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.ArrayList;
import java.util.List;

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

    public void eventPromotionClick(int position, String label, String imgUrl) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PROMOTION,
                position + "-" + label + "-" + imgUrl
        );
    }

    public void eventTripTypeClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_TRIP_TYPE,
                label
        );
    }

    public void eventOriginClick(String cityName, String airportId) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_ORIGIN,
                cityName + "|" + airportId
        );
    }

    public void eventDestinationClick(String cityName, String airportId) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_DESTINATION,
                cityName + "|" + airportId
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

    public void eventSearchProductClick(FlightSearchViewModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getAirlineList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineDB airlineDB : viewModel.getAirlineList()) {
                airlines.add(airlineDB.getId());
            }
            result.append(TextUtils.join(",", airlines));
        }

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String timeResult = viewModel.getRouteList().get(0).getDepartureTimestamp();
            timeResult += "-" + viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp();
            result.append(timeResult);
        }
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        );
    }

    public void eventSearchDetailClick(FlightSearchViewModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getAirlineList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineDB airlineDB : viewModel.getAirlineList()) {
                airlines.add(airlineDB.getId());
            }
            result.append(TextUtils.join(",", airlines));
        }

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String timeResult = viewModel.getRouteList().get(0).getDepartureTimestamp();
            timeResult += "-" + viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp();
            result.append(timeResult);
        }
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_DETAIL,
                result.toString()
        );
    }

    public void eventDetailPriceTabClick(FlightDetailViewModel viewModel) {
        StringBuilder result = new StringBuilder();

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            List<String> airlines = new ArrayList<>();
            for (FlightDetailRouteViewModel airlineDB : viewModel.getRouteList()) {
                if (!airlines.contains(airlineDB.getAirlineCode())) {
                    airlines.add(airlineDB.getAirlineCode());
                }
            }
            result.append(TextUtils.join(",", airlines));

            String timeResult = viewModel.getRouteList().get(0).getDepartureTimestamp();
            timeResult += "-" + viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp();
            result.append(timeResult);
        }
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PRICE_TAB,
                result.toString()
        );
    }

    public void eventDetailFacilitiesTabClick(FlightDetailViewModel viewModel) {
        StringBuilder result = new StringBuilder();

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            List<String> airlines = new ArrayList<>();
            for (FlightDetailRouteViewModel airlineDB : viewModel.getRouteList()) {
                if (!airlines.contains(airlineDB.getAirlineCode())) {
                    airlines.add(airlineDB.getAirlineCode());
                }
            }
            result.append(TextUtils.join(",", airlines));

            String timeResult = viewModel.getRouteList().get(0).getDepartureTimestamp();
            timeResult += "-" + viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp();
            result.append(timeResult);
        }
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_FACILITIES_TAB,
                result.toString()
        );
    }

    public void eventDetailTabClick(FlightDetailViewModel viewModel) {
        StringBuilder result = new StringBuilder();

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            List<String> airlines = new ArrayList<>();
            for (FlightDetailRouteViewModel airlineDB : viewModel.getRouteList()) {
                if (!airlines.contains(airlineDB.getAirlineCode())) {
                    airlines.add(airlineDB.getAirlineCode());
                }
            }
            result.append(TextUtils.join(",", airlines));

            String timeResult = viewModel.getRouteList().get(0).getDepartureTimestamp();
            timeResult += "-" + viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp();
            result.append(timeResult);
        }
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_DETAIL_TAB,
                result.toString()
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

    public void eventPassengerClick(int adult, int children, int infant) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_PASSENGER,
                adult + "-" + children + "-" + infant
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

    private static class Label {
        static String NORMAL_PRICE = "Normal Price";
    }

}
