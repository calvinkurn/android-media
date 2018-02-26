package com.tokopedia.ride.analytics;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * Created by sandeepgoyal on 10/10/17.
 */

public class RideGATracking extends UnifyTracking {
    public static void eventBackPress(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickBack),
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventUberOpenViaShortcut(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventOpenUberUsingShortcut,
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickOnTrip(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickOnYourTrips,
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickSource(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickSource,
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickDestination(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickDestination,
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickDeleteDestination(String addressDeleted) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickDeleteDestination,
                String.format(RideAppEventTracking.Label.AddressDeleted, addressDeleted)
        ).getEvent());
    }


    public static void eventSelectRideOption(String optionType, String time, String fare) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventSelectRideOption,
                String.format(RideAppEventTracking.Label.RideDetail, optionType, time, fare)
        ).getEvent());
    }

    public static void eventDeletePromotion(String screenName, String promotionName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventDeletePromotion,
                String.format(RideAppEventTracking.Label.PromotionName, toLowerCase(screenName), promotionName)
        ).getEvent());
    }

    public static void eventClickRequestRideOption(String screenName, String rideOption) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickRideOption, rideOption),
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickAutDetectLocation(String screenName, String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventAutoDetectCurrentLocation),
                String.format(RideAppEventTracking.Label.AddressSelected, toLowerCase(screenName), addressSelected)
        ).getEvent());
    }

    public static void eventClickSourceOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSourceOpenMap),
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickSourceRecentAddress(String screenName, String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSourceRecentAddress),
                String.format(RideAppEventTracking.Label.AddressSelected, screenName, addressSelected)
        ).getEvent());
    }

    public static void eventClickDoneSourceMap(String screenName, String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickDoneSourceMap,
                String.format(RideAppEventTracking.Label.AddressSelected, toLowerCase(screenName), addressSelected)
        ).getEvent());
    }

    public static void eventClickDestinationOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickDestinationOpenMap),
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }


    public static void eventClickDestinationRecentAddress(String screenName, String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickDestinationRecentAddress),
                String.format(RideAppEventTracking.Label.AddressSelected, toLowerCase(screenName), addressSelected)
        ).getEvent());

    }

    public static void eventClickHelpTrip(String screenName, String date, String fare, String bookingStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickHelpTrip,
                String.format(RideAppEventTracking.Label.BookingDetails, toLowerCase(screenName), date, fare, bookingStatus)
        ).getEvent());
    }

    public static void eventSelectHelpOption(String helpOption) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventSelectHelpOption,
                String.format(RideAppEventTracking.Label.HelpOption, helpOption)
        ).getEvent());
    }

    public static void eventClickApplyPromoSearch(String screenName, String promoSearchValue) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickApplyPromoSearch,
                String.format(RideAppEventTracking.Label.PromoSearchValue, toLowerCase(screenName), promoSearchValue)
        ).getEvent());
    }

    public static void eventClickApplyOffers(String screenName, String promoCode) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickApplyOffers,
                String.format(RideAppEventTracking.Label.PromoCode, toLowerCase(screenName), promoCode)
        ).getEvent());
    }

    public static void eventClickReadOfferDetails(String screenName, String promoCode) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickReadOfferDetails,
                String.format(RideAppEventTracking.Label.PromoCode, toLowerCase(screenName), promoCode)
        ).getEvent());
    }

    public static void eventClickCancelRequestRide(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickCancelRequestRide,
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    public static void eventClickCancelReason(String screenName, String reason) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventSelectCancelReason,
                String.format(RideAppEventTracking.Label.CancelReason, toLowerCase(screenName), reason)
        ).getEvent());
    }


    public static void eventClickChangeDestinationOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventChangeDestiantionMap,
                toLowerCase(screenName)
        ).getEvent());
    }

    public static void eventClickDoneDestinationMap(String screenName, String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventDoneOnDestinationMap),
                String.format(RideAppEventTracking.Label.AddressSelected, toLowerCase(screenName), addressSelected)
        ).getEvent());
    }

    public static void eventClickReceipt(String screenName, String date, String fare, String bookingStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickReceipt),
                String.format(RideAppEventTracking.Label.BookingDetails, toLowerCase(screenName), date, fare, bookingStatus)
        ).getEvent());
    }

    public static void eventClickCall(String rideStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickCall,
                rideStatus
        ).getEvent());

    }

    public static void eventClickSMS(String rideStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickSmS,
                rideStatus
        ).getEvent());

    }

    public static void eventClickShareEta(String rideStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickShareEta,
                rideStatus
        ).getEvent());
    }

    public static void eventClickCancel(String rideStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickCancel,
                rideStatus
        ).getEvent());
    }

    public static void eventClickSubmit(String rating, String suggestions) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSubmit),
                String.format(AppScreen.SCREEN_RIDE_COMPLETED + "- %s - %s", rating, suggestions)
        ).getEvent());
    }

    public static void eventClickTNC() {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickTNC),
                AppScreen.SCREEN_RIDE_COMPLETED
        ).getEvent());
    }

    public static void eventClickSignup() {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSignup),
                AppScreen.SCREEN_RIDE_COMPLETED
        ).getEvent());
    }

    public static void eventOpenInterruptScreen(String url) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventOpenInterrupt,
                url
        ).getEvent());
    }

    public static void eventUberCreateShortcut(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventCreateShortcut,
                String.format(RideAppEventTracking.Label.ScreenName, toLowerCase(screenName))
        ).getEvent());
    }

    private static String toLowerCase(String screenName) {
        if (screenName == null || screenName.isEmpty()) {
            return "Uber Screen";
        }

        return screenName.toLowerCase();
    }
}
