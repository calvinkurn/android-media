package com.tokopedia.flight.common.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                String.format(getDefaultLocale(), "%d-%s-%s", position, label, imgUrl)
        );
    }

    private Locale getDefaultLocale() {
        return Locale.getDefault();
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
        StringBuilder result = transformSearchProductClickLabel(viewModel);
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        );
    }


    public void eventSearchProductClick(FlightSearchViewModel viewModel, int adapterPosition) {
        StringBuilder result = transformSearchProductClickLabel(viewModel);
        result.append(String.format(getDefaultLocale(), " - %d", adapterPosition));
        result.append(Label.NORMAL_PRICE);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_PRODUCT,
                result.toString()
        );
    }

    @NonNull
    private StringBuilder transformSearchProductClickLabel(FlightSearchViewModel viewModel) {
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
            timeResult += " - " + viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp();
            result.append(timeResult);
        }
        return result;
    }

    public void eventSearchDetailClick(FlightSearchViewModel viewModel, int adapterPosition) {
        StringBuilder result = transformSearchDetailLabel(viewModel, adapterPosition);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_SEARCH_DETAIL,
                result.toString()
        );
    }

    public void eventDetailPriceTabClick(FlightDetailViewModel viewModel) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_PRICE_TAB,
                transformEventDetailLabel(viewModel)
        );
    }

    public void eventDetailFacilitiesTabClick(FlightDetailViewModel viewModel) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_FACILITIES_TAB,
                transformEventDetailLabel(viewModel)
        );
    }

    public void eventDetailTabClick(FlightDetailViewModel viewModel) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.CLICK_DETAIL_TAB,
                transformEventDetailLabel(viewModel)
        );
    }

    private String transformEventDetailLabel(FlightDetailViewModel viewModel) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            result.append(transformAirlines(viewModel));

            String timeResult = String.format(" - %s", viewModel.getRouteList().get(0).getDepartureTimestamp());
            timeResult += String.format(" - %s ", viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp());
            result.append(timeResult);
        }
        result.append(transformRefundableLabel(viewModel.getIsRefundable()));
        result.append(Label.NORMAL_PRICE);
        return result.toString();
    }

    @NonNull
    private String transformAirlines(FlightDetailViewModel viewModel) {
        List<String> airlines = new ArrayList<>();
        for (FlightDetailRouteViewModel airlineDB : viewModel.getRouteList()) {
            if (!airlines.contains(airlineDB.getAirlineCode())) {
                airlines.add(airlineDB.getAirlineCode());
            }
        }
        return TextUtils.join(",", airlines);
    }

    @NonNull
    private StringBuilder transformSearchDetailLabel(FlightSearchViewModel viewModel, int adapterPosition) {
        StringBuilder result = new StringBuilder();
        if (viewModel.getAirlineList() != null) {
            List<String> airlines = new ArrayList<>();
            for (FlightAirlineDB airlineDB : viewModel.getAirlineList()) {
                airlines.add(airlineDB.getId());
            }
            result.append(TextUtils.join(",", airlines));
        }

        if (viewModel.getRouteList() != null && viewModel.getRouteList().size() > 0) {
            String timeResult = String.format(" - %s", viewModel.getRouteList().get(0).getDepartureTimestamp());
            timeResult += String.format(" - %s", viewModel.getRouteList().get(viewModel.getRouteList().size() - 1).getArrivalTimestamp());
            result.append(timeResult);
        }
        result.append(transformRefundableLabel(viewModel.isRefundable()));
        result.append(String.format(getDefaultLocale(), " - %d", adapterPosition));
        result.append(Label.NORMAL_PRICE);
        return result;
    }


    @NonNull
    private String transformRefundableLabel(RefundableEnum refundableEnum) {
        String refundable;
        if (refundableEnum == RefundableEnum.REFUNDABLE) {
            refundable = Label.REFUNDABLE;
        } else if (refundableEnum == RefundableEnum.PARTIAL_REFUNDABLE) {
            refundable = Label.PARTIALLY_REFUNDABLE;
        } else {
            refundable = Label.NOT_REFUNDABLE;
        }
        return refundable;
    }

    public void eventDetailClick(FlightDetailViewModel viewModel) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.BOOKING_DETAIL,
                transformEventDetailLabel(viewModel)
        );
    }

    public void eventBookingNextClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.BOOKING_NEXT,
                label
        );
    }

    public void eventReviewNextClick() {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.REVIEW_NEXT,
                Label.REVIEW_NEXT
        );
    }

    public void eventVoucherClick(String label) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER,
                label
        );
    }

    public void eventVoucherSuccess(String label, String message) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER_SUCCESS,
                String.format("%s- %s", label, message)
        );
    }

    public void eventVoucherErrors(String label, String message) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.VOUCHER_ERROR,
                String.format("%s- %s", label, message)
        );
    }

    private void eventAddToCart(FlightDetailViewModel viewModel) {

        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.ADD_TO_CART,
                transformEventDetailLabel(viewModel)
        );
    }

    public void eventPassengerClick(int adult, int children, int infant) {
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.SELECT_PASSENGER,
                String.format(getDefaultLocale(),
                        "%d %s - %d %s - %d %s",
                        adult,
                        Label.ADULT,
                        children,
                        Label.CHILD,
                        infant,
                        Label.INFANT
                )
        );
    }

    public void eventAddToCart(FlightDetailViewModel departureViewModel, FlightDetailViewModel returnViewModel) {
        if (departureViewModel != null)
            eventAddToCart(departureViewModel);
        if (returnViewModel != null)
            eventAddToCart(returnViewModel);
    }

    public void eventPromoImpression(int position, BannerDetail bannerData) {
        analyticTracker.sendEventTracking(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PROMOTION_IMPRESSION,
                String.format(getDefaultLocale(),
                        "%d - %s - %s",
                        position + 1,
                        bannerData.getAttributes().getTitle(),
                        bannerData.getAttributes().getImgUrl()
                )
        );
    }

    public void eventProductDetailImpression(FlightSearchViewModel flightSearchViewModel, int adapterPosition) {
        StringBuilder result = transformSearchDetailLabel(flightSearchViewModel, adapterPosition);
        analyticTracker.sendEventTracking(GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PRODUCT_DETAIL_IMPRESSION,
                result.toString()
        );
    }

    public void eventPurchaseAttemptSuccess() {
        analyticTracker.sendEventTracking(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                Label.SUCCESS_PURCHASE
        );
    }

    public void eventPurchaseAttemptFailed() {
        analyticTracker.sendEventTracking(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                Label.FAILED_PURCHASE
        );
    }

    public void eventPurchaseAttemptCancelled() {
        analyticTracker.sendEventTracking(
                GENERIC_EVENT,
                GENERIC_CATEGORY,
                Category.PURCHASE_ATTEMPT,
                Label.CANCEL_PURCHASE
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
        static String PROMOTION_IMPRESSION = "promotion impressions";
        static String CLICK_TRIP_TYPE = "select trip type";
        static String SELECT_ORIGIN = "select origin";
        static String SELECT_DESTINATION = "select destination";
        static String SELECT_PASSENGER = "select Passenger";
        static String SELECT_CLASS = "select flight class";
        static String CLICK_SEARCH = "click search flight";
        static String CLICK_SEARCH_PRODUCT = "product click";
        static String CLICK_SEARCH_DETAIL = "click see the details";
        static String PRODUCT_DETAIL_IMPRESSION = "product detail impression";
        static String CLICK_PRICE_TAB = "click price tab";
        static String CLICK_FACILITIES_TAB = "click facilities tab";
        static String CLICK_DETAIL_TAB = "click flights detail tab";
        static String ADD_TO_CART = "add to cart";
        static String BOOKING_DETAIL = "click detail";
        static String BOOKING_NEXT = "click next";
        static String REVIEW_NEXT = "click next";
        static String VOUCHER = "click gunakan voucher code";
        static String VOUCHER_SUCCESS = "voucher success";
        static String VOUCHER_ERROR = "voucher error";
        static String PURCHASE_ATTEMPT = "purchase attempt";

    }

    private static class Label {
        static String FAILED_PURCHASE = "FAILED";
        static String SUCCESS_PURCHASE = "SUCCESS";
        static String CANCEL_PURCHASE = "CANCEL";
        static String NORMAL_PRICE = " - Normal Price";
        static String ADULT = "adult";
        static String CHILD = "child";
        static String INFANT = "baby";
        static String REVIEW_NEXT = " on order details page";
        static String REFUNDABLE = "- refundable";
        static String NOT_REFUNDABLE = "- not refundable";
        static String PARTIALLY_REFUNDABLE = "- partially refundable";
    }

}
