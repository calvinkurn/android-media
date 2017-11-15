package com.tokopedia.flight.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.search.data.cloud.model.request.FlightSearchSingleRequestData;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightApi {
    @GET(FlightUrl.FLIGHT_CLASS_PATH)
    Observable<Response<DataResponse<List<FlightClassEntity>>>> getFlightClasses();

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_SEARCH_SINGLE)
    Observable<Response<DataResponse<List<FlightSearchData>>>> searchFlightSingle(@Body DataRequest<FlightSearchSingleRequestData> flightSearchRequest);

    @GET(FlightUrl.FLIGHT_AIRPORT_PATH)
    Observable<Response<DataResponse<List<FlightAirportCountry>>>> getFlightAirportList(@QueryMap Map<String, String> keyword);

    @GET(FlightUrl.FLIGHT_AIRLINE_PATH)
    Observable<Response<DataResponse<List<AirlineData>>>> getFlightAirlineList();

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CART_PATH)
    Observable<Response<DataResponse<CartEntity>>> addCart(@Body DataRequest<FlightCartRequest> request, @Header("Idempotency-Key") String idemPotencyKeyHeader);
}
