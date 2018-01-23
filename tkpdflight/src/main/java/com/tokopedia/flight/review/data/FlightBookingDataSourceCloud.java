package com.tokopedia.flight.review.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.common.di.qualifier.BookingQualifier;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 12/7/17.
 */

public class FlightBookingDataSourceCloud {

    private final FlightApi flightApi;
    private UserSession userSession;
    private Gson gson;

    @Inject
    public FlightBookingDataSourceCloud(FlightApi flightApi, UserSession userSession, @BookingQualifier Gson gson) {
        this.flightApi = flightApi;
        this.userSession = userSession;
        this.gson = gson;
    }

    public Observable<DataResponseVerify> verifyBooking(VerifyRequest verifyRequest) {
        return flightApi.verifyBooking(gson
                .fromJson(gson.toJson(verifyRequest), JsonElement.class).getAsJsonObject(), userSession.getUserId())
                .flatMap(new Func1<Response<DataResponse<DataResponseVerify>>, Observable<DataResponseVerify>>() {
                    @Override
                    public Observable<DataResponseVerify> call(Response<DataResponse<DataResponseVerify>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                });
    }

    public Observable<FlightCheckoutEntity> checkout(FlightCheckoutRequest request) {
        return flightApi.checkout(request, userSession.getUserId())
                .map(new Func1<Response<DataResponse<FlightCheckoutEntity>>, FlightCheckoutEntity>() {
                    @Override
                    public FlightCheckoutEntity call(Response<DataResponse<FlightCheckoutEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
