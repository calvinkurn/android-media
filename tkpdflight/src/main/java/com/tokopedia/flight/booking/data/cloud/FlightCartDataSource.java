package com.tokopedia.flight.booking.data.cloud;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 11/13/17.
 */

public class FlightCartDataSource {
    private FlightApi flightApi;
    private UserSession userSession;

    @Inject
    public FlightCartDataSource(FlightApi flightApi, UserSession userSession) {
        this.flightApi = flightApi;
        this.userSession = userSession;
    }

    public Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey) {
        return this.flightApi.addCart(new DataRequest<>(request), idEmpotencyKey, userSession.getUserId())
                .map(new Func1<Response<DataResponse<CartEntity>>, CartEntity>() {
                    @Override
                    public CartEntity call(Response<DataResponse<CartEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
