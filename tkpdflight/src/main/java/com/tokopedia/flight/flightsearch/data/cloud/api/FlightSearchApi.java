package com.tokopedia.flight.flightsearch.data.cloud.api;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.flightsearch.data.db.FlightSearchSingleRouteDB;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by User on 10/27/2017.
 */

public interface FlightSearchApi {

    @POST(FlightUrl.FLIGHT_SEARCH_SINGLE)
    Observable<Response<DataResponse<List<FlightSearchSingleRouteDB>>>> searchFlightSingle(@Body JsonObject requestBody);

}
