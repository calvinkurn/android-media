package com.tokopedia.flight.airport.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.common.constant.FlightUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface FlightAirportApi {

    @GET(FlightUrl.FLIGHT_AIRPORT_PATH)
    Observable<Response<DataResponse<List<FlightAirportCountry>>>> getFlightAirportList(@QueryMap Map<String, String> keyword);
}
