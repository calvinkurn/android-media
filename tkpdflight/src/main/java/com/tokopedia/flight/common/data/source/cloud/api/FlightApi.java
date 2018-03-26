package com.tokopedia.flight.common.data.source.cloud.api;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.passenger.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.passenger.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;
import com.tokopedia.flight.passenger.data.cloud.requestbody.UpdatePassengerRequest;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.search.data.cloud.model.request.FlightSearchSingleRequestData;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by alvarisi on 10/30/17.
 */

public interface FlightApi {
    @GET(FlightUrl.FLIGHT_CLASS_PATH)
    Observable<Response<DataResponse<List<FlightClassEntity>>>> getFlightClasses();

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_SEARCH_SINGLE)
    Observable<Response<FlightDataResponse<List<FlightSearchData>>>> searchFlightSingle(@Body DataRequest<FlightSearchSingleRequestData> flightSearchRequest);

    @GET(FlightUrl.FLIGHT_AIRPORT_PATH)
    Observable<Response<DataResponse<List<FlightAirportCountry>>>> getFlightAirportList(@QueryMap Map<String, String> keyword);

    @GET(FlightUrl.FLIGHT_AIRLINE_PATH)
    Observable<Response<DataResponse<List<AirlineData>>>> getFlightAirlineList();

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CART_PATH)
    Observable<Response<DataResponse<CartEntity>>> addCart(@Body DataRequest<FlightCartRequest> request,
                                                           @Header("Idempotency-Key") String idemPotencyKeyHeader,
                                                           @Header("x-tkpd-userid") String userId
    );

    @GET(FlightUrl.FLIGHT_CHECK_VOUCHER_CODE)
    Observable<Response<DataResponse<AttributesVoucher>>> checkVoucherCode(@QueryMap HashMap<String, String> paramsAllValueInString);

    @GET(FlightUrl.FLIGHT_ORDERS)
    Observable<Response<DataResponse<List<OrderEntity>>>> getOrders(@QueryMap Map<String, Object> paramsAllValueInString);

    @GET(FlightUrl.FLIGHT_ORDER)
    Observable<Response<DataResponse<OrderEntity>>> getOrder(@Path("id") String id);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_VERIFY_BOOKING)
    Observable<Response<DataResponse<DataResponseVerify>>> verifyBooking(@Body JsonObject verifyRequest, @Header("x-tkpd-userid") String userId);

    @Headers({"Content-Type: application/json"})
    @POST(FlightUrl.FLIGHT_CHECKOUT_BOOKING)
    Observable<Response<DataResponse<FlightCheckoutEntity>>> checkout(@Body FlightCheckoutRequest checkoutRequest, @Header("x-tkpd-userid") String userId);

    @GET
    Observable<Response<DataResponse<List<BannerDetail>>>> getBanners(@Url String url, @QueryMap Map<String, String> params);

    @GET(FlightUrl.FLIGHT_AIRLINE_PATH)
    Observable<Response<DataResponse<List<AirlineData>>>> getFlightAirline(@Query("id") String airlineId);

    @GET(FlightUrl.FLIGHT_EMAIL)
    Observable<Response<SendEmailEntity>> sendEmail(@QueryMap Map<String, Object> param);

    @GET(FlightUrl.FLIGHT_PASSENGER_SAVED)
    Observable<Response<FlightDataResponse<List<SavedPassengerEntity>>>> getSavedPassengerData();

    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = FlightUrl.FLIGHT_PASSENGER_SAVED, hasBody = true)
    Observable<Response<Object>> deleteSavedPassengerData(@Body DataRequest<DeletePassengerRequest> request,
                                                          @Header("Idempotency-Key") String idemPotencyKeyHeader);

    @Headers({"Content-Type: application/json"})
    @PATCH(FlightUrl.FLIGHT_PASSENGER_SAVED)
    Observable<Response<Object>> updatePassengerListData(@Body DataRequest<UpdatePassengerRequest> request,
                                                         @Header("Idempotency-Key") String idemPotencyKeyHeader);
}
