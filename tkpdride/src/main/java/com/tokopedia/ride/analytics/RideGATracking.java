package com.tokopedia.ride.analytics;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.ride.common.configuration.RideStatus;

/**
 * Created by sandeepgoyal on 10/10/17.
 */

public class RideGATracking extends UnifyTracking {
    public void eventBackPress(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                String.format(RideAppEventTracking.Action.EventClickBack, screenName),
                RideAppEventTracking.Label.No_Label
        ).getEvent());
    }

    public void eventClickOnTrip() {

    }

    public void eventClickSource() {

    }

    public void eventClickDestination() {

    }

    public void eventClickDeleteDestination(String addressDeleted) {

    }


    public void eventSelectRideOption(String optionType, String time, String fare) {

    }

    public void eventDeletePromotion(String promotionName) {

    }

    public void eventClickRequestRideOption(String rideOption) {

    }

    public void eventClickAutDetectLocation() {

    }

    public void eventClickSourceOpenMap(String screenName, String AddressSelected) {

    }

    public void eventClickSourceRecentAddress(String screenName) {

    }

    public void eventClickDoneSourceMap(String addressSelected) {

    }

    public void eventClickDestinationOpenMap(String screenName) {

    }


    public void eventClickDestinationRecentAddress(String screenName, String AddressSelected) {

    }

    public void eventClickHelpTrip(String date, String fare, String bookingStatus) {

    }

    public void eventSelectHelpOption(String helpOption) {

    }

    public void eventClickApplyPromoSearch(String promoSearchValue) {

    }

    public void eventClickApplyOffers(String promoCode) {

    }

    public void eventClickReadOfferDetails(String promoCode) {

    }

    public void eventClickCancelRequestRide() {

    }

    public void eventClickCancelReason(String reason) {

    }

    public static void eventClickChangeDestinationOpenMap(String screenName) {
        sendGTMEvent(new EventTracking(
                RideAppEventTracking.Event.GenericUberEvent,
                RideAppEventTracking.Category.EventCategory,
                RideAppEventTracking.Action.EventChangeDestiantionMap,
                screenName
        ).getEvent());
    }

    public static void eventClickDoneDestinationMap() {

    }

    public static void eventClickReceipt(String date, String fare, String bookingStatus) {

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
                String.format(AppScreen.SCREEN_RIDE_COMPLETED + "- %s - %s" , rating , suggestions)
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
}
