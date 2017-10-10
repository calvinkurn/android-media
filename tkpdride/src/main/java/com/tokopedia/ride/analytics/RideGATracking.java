package com.tokopedia.ride.analytics;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * Created by sandeepgoyal on 10/10/17.
 */

public class RideGATracking extends UnifyTracking{
        public void eventBackPress(String screenName) {
            sendGTMEvent(new EventTracking(
                    RideAppEventTracking.Event.GenericUberEvent,
                    RideAppEventTracking.Category.EventCategory,
                    String.format(RideAppEventTracking.Action.EventClickBack,screenName),
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


        public void eventSelectRideOption(String optionType,String time, String fare) {

        }

        public void eventDeletePromotion(String promotionName) {

        }

        public void eventClickRequestRideOption(String rideOption) {

        }

        public void eventClickAutDetectLocation() {

        }

        public void eventClickSourceOpenMap(String screenName,String AddressSelected) {

        }

        public void eventClickSourceRecentAddress(String screenName) {

        }

        public void eventClickDoneSourceMap(String addressSelected) {

        }

        public void eventClickDestinationOpenMap(String screenName) {

        }



        public void eventClickDestinationRecentAddress(String screenName,String AddressSelected) {

        }

        public void eventClickHelpTrip(String date,String fare,String bookingStatus) {

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

        public void eventClickChangeDestinationOpenMap(String screenName) {

        }
        public void eventClickDoneDestinationMap() {

        }
        public void eventClickReceipt(String date,String fare,String bookingStatus) {

        }

        public void eventClickCall(String screenName) {

        }
        public void eventClickSMS(String screenName) {

        }

        public void eventClickShareEta(String screenName) {

        }
        public void eventClickCancel(String screenName) {

        }

        public void eventClickSubmit() {

        }
        public void eventClickTNC() {

        }
}
