package com.tokopedia.ride.analytics;

import com.tokopedia.core.analytics.AppEventTracking;

/**
 * Created by sandeepgoyal on 10/10/17.
 */

public interface RideAppEventTracking extends AppEventTracking {
    interface Event {
        String GenericUberEvent = "genericUberEvent";
    }

    interface Category {
        String EventCategory = "digital - uber";
    }

    interface Action {
        String EventClickBack = "click back";
        String EventClickOnYourTrips = "click on your trips";
        String EventClickSource = "click source";
        String EventClickDestination = "click destination";
        String EventClickDeleteDestination = "click delete destination";
        String EventSelectRideOption = "select ride option";
        String EventDeletePromotion = "delete promotion";
        String EventClickRideOption = "click request %s";
        String EventAutoDetectCurrentLocation = "click autodetect current location";
        String EventClickSourceOpenMap = "click source open map on";
        String EventClickSourceRecentAddress = "click source recent addresses";
        String EventClickDoneSourceMap = "click done on source map";
        String EventClickDestinationOpenMap = "click destination open map";
        String EventClickDestinationRecentAddress = "click destination recent addresses";
        String EventDoneOnDestinationMap = "click done on destination map";
        String EventClickReceipt = "click receipt";
        String EventClickHelpTrip = "click help for trip details";
        String EventSelectHelpOption = "select help option";
        String EventClickApplyPromoSearch = "click apply promo search";
        String EventClickApplyOffers = "click apply offers";
        String EventClickReadOfferDetails = "click read offer details";
        String EventClickCancelRequestRide = "click cancel request ride";
        String EventSelectCancelReason = "select cancel reason";
        String EventChangeDestiantionMap = "click change destination open map";
        String EventClickCall = "click call";
        String EventClickSmS = "click sms";
        String EventClickShareEta = "click share eta";
        String EventClickCancel = "click cancel";
        String EventClickSubmit = "click submit";
        String EventClickTNC = "click tnc";
        String EventClickSignup = "click sign up uber";
        String EventOpenInterrupt = "open interrupt screen";
    }

    interface Label {
        String No_Label = "";
        String ScreenName = "%s";
        String AddressDeleted = "%s";
        String RideDetail = "%s - %s - %s";
        String PromotionName = ScreenName + " - %s";
        String AddressSelected = ScreenName + " - %s";
        String BookingDetails = ScreenName + " -%s - %s - %s";
        String HelpOption = "%s";
        String PromoSearchValue = ScreenName + " - %s";
        String PromoCode = ScreenName + " - %s";
        String CancelReason = ScreenName + " - %s";
        String RateSuggestion = ScreenName + " - %s - %s";
    }
}
