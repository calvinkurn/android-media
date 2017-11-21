package com.tokopedia.flight.booking.data.cloud;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
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

    @Inject
    public FlightCartDataSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey) {
        return this.flightApi.addCart(new DataRequest<>(request), idEmpotencyKey, "3816346")
                .map(new Func1<Response<DataResponse<CartEntity>>, CartEntity>() {
                    @Override
                    public CartEntity call(Response<DataResponse<CartEntity>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
