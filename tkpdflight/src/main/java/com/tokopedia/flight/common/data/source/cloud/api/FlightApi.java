package com.tokopedia.flight.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.source.cloud.entity.flightclass.FlightClassEntity;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightApi {
    @GET(FlightUrl.FLIGHT_CLASS_PATH)
    Observable<Response<DataResponse<List<FlightClassEntity>>>> getFlightClasses();
}
