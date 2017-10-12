package com.tokopedia.ride.analytics;

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
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickOnTrip(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickOnYourTrips,
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickSource(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickSource,
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickDestination(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickDestination,
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickDeleteDestination(String addressDeleted) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickDeleteDestination,
                String.format(RideAppEventTracking.Label.AddressDeleted,addressDeleted)
        ).getEvent());
    }


    public static void eventSelectRideOption(String optionType,String time, String fare) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventSelectRideOption,
                String.format(RideAppEventTracking.Label.RideDetail,optionType,time,fare)
        ).getEvent());
    }

    public static void eventDeletePromotion(String screenName,String promotionName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventDeletePromotion,
                String.format(RideAppEventTracking.Label.PromotionName,screenName,promotionName)
        ).getEvent());
    }

    public static void eventClickRequestRideOption(String screenName,String rideOption) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickRideOption,rideOption),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickAutDetectLocation(String screenName,String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventAutoDetectCurrentLocation),
                String.format(RideAppEventTracking.Label.AddressSelected,screenName,addressSelected)
        ).getEvent());
    }

    public static void eventClickSourceOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSourceOpenMap),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickSourceRecentAddress(String screenName,String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSourceRecentAddress),
                String.format(RideAppEventTracking.Label.AddressSelected,screenName,addressSelected)
        ).getEvent());
    }

    public static void eventClickDoneSourceMap(String screenName,String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickDoneSourceMap,
                String.format(RideAppEventTracking.Label.AddressSelected,screenName,addressSelected)
        ).getEvent());
    }

    public static void eventClickDestinationOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickDestinationOpenMap),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }



    public static void eventClickDestinationRecentAddress(String screenName,String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickDestinationRecentAddress),
                String.format(RideAppEventTracking.Label.AddressSelected,screenName,addressSelected)
        ).getEvent());

    }

    public static void eventClickHelpTrip(String screenName,String date,String fare,String bookingStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickHelpTrip,
                String.format(RideAppEventTracking.Label.BookingDetails,screenName,date,fare,bookingStatus)
        ).getEvent());
    }
    public static void eventSelectHelpOption(String helpOption) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventSelectHelpOption,
                String.format(RideAppEventTracking.Label.HelpOption,helpOption)
        ).getEvent());
    }

    public static void eventClickApplyPromoSearch(String screenName,String promoSearchValue) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickApplyPromoSearch,
                String.format(RideAppEventTracking.Label.PromoSearchValue,screenName,promoSearchValue)
        ).getEvent());
    }

    public static void eventClickApplyOffers(String screenName,String promoCode) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickApplyOffers,
                String.format(RideAppEventTracking.Label.PromoCode,screenName,promoCode)
        ).getEvent());
    }

    public static void eventClickReadOfferDetails(String screenName,String promoCode) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickReadOfferDetails,
                String.format(RideAppEventTracking.Label.PromoCode,screenName,promoCode)
        ).getEvent());
    }

    public static void eventClickCancelRequestRide(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventClickCancelRequestRide,
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickCancelReason(String screenName,String reason) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventSelectCancelReason,
                String.format(RideAppEventTracking.Label.CancelReason,screenName,reason)
        ).getEvent());
    }


    public static void eventClickChangeDestinationOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventChangeDestiantionMap),
                        String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickDoneDestinationMap(String screenName,String addressSelected) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventDoneOnDestinationMap),
                String.format(RideAppEventTracking.Label.AddressSelected,screenName,addressSelected)
        ).getEvent());
    }

    public static void eventClickReceipt(String screenName,String date, String fare, String bookingStatus) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickReceipt),
                String.format(RideAppEventTracking.Label.BookingDetails,screenName,date,fare,bookingStatus)
        ).getEvent());
    }

    public static void eventClickCall(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickCall),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());

    }

    public static void eventClickSMS(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSmS),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());

    }

    public static void eventClickShareEta(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickShareEta),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickCancel(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickCancel),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }

    public static void eventClickSubmit(String screenName,String rating, String suggestions) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickSubmit),
                String.format(RideAppEventTracking.Label.RateSuggestion , screenName,rating , suggestions)
        ).getEvent());
    }

    public static void eventClickTNC(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickTNC),
                String.format(RideAppEventTracking.Label.ScreenName,screenName)
        ).getEvent());
    }
}
