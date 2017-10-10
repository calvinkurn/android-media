package com.tokopedia.ride.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

/**
 * Created by sandeepgoyal on 10/10/17.
 */

public interface RideAppEventTracking extends AppEventTracking {
    interface Event {
        String GenericUberEvent = "GenericUberEvent";
    }
    interface  Category {
        String EventCategory ="uber";
    }
    interface Action {
        String EventClickBack = "click back on %s";
        String EventClickOnYourTrips = "click on your trips";
        String EventClickSource = "click source";
        String EventClickDestination = "click destination";
        String EventClickDeleteDestination = "click delete destination";
        String EventSelectRideOption = "select ride option";
        String EventDeletePromotion = "delete promotion";
        String EventClickRideOption = "click reqeust %s";
        String EventAutoDetectCurrentLocation  = "click autodetect current location on %s";
        String EventClickSourceOpenMap = "click source open map on %s";
        String EventClickDoneSourceMap = "click done on source map";
        String EventClickDestinationOpenMap = "click destiantion open map on %s";
        String EventClickDestinationRecentAddress = "click destination recent addresses on %s";
        String EventDoneOnDestinationMap = "click done on destination map";
        String EventClickReceipt = "click receipt";
        String EventClickHelpTrip ="click help for trip details";
        String EventSelectHelpOption = "select help option";
        String EventClickApplyPromoSearch = "click apply promo search";
        String EventClickApplyOffers = "click apply offers";
        String EventClickReadOfferDetails = "click read offer details";
        String EventClickCancelRequestRide = "click cancel request ride";
        String EventSelectCancelReason = "select cancel %s";
        String EventChangeDestiantionMap = "click change destination open map on %s";
        String EventClickCall = "click call on %s";
        String EventClickSmS  = "click sms on %s";
        String EventClickShareEta = "click share on %s";
        String EventClickCancel = "click cancel on  %s";
        String EventClickSubmit  = "click submit";
        String EventClickTNC = "click tnc";

    }
    interface  Label {
        String No_Label = "";
        String AddressDeleted = "AddressDeleted";
        String RideDetail = "%s - %s -%s";
        String PromotionName = "%s";
        String AddressSelected = "AddressSelected";
        String BookingDetails = "%s - %s - %s";
        String HelpOption = "%s";
        String PromoSearchValue = "%s";
        String PromoCode = "%s";
        String CancelReason = "%s";
        String RateSuggestion ="%s - %s";


    }
}
