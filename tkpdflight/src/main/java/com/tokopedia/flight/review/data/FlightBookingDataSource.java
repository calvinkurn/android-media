package com.tokopedia.flight.review.data;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 12/7/17.
 */

public class FlightBookingDataSource {


    private FlightBookingDataSourceCloud flightBookingDataSourceCloud;

    @Inject
    public FlightBookingDataSource(FlightBookingDataSourceCloud flightBookingDataSourceCloud) {
        this.flightBookingDataSourceCloud = flightBookingDataSourceCloud;
    }

    public Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest) {
        return flightBookingDataSourceCloud.verifyBooking(verifyRequest);
    }

    public Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request) {
        return flightBookingDataSourceCloud.checkout(request);
    }


}
