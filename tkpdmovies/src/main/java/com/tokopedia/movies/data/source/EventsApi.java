package com.tokopedia.movies.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.movies.data.entity.response.EventLocationEntity;
import com.tokopedia.movies.data.entity.response.EventResponseEntity;
import com.tokopedia.movies.data.entity.response.EventsDetailsEntity;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.ValidateResponse;
import com.tokopedia.movies.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.data.entity.response.verifyresponse.VerifyCartResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface EventsApi {
    @GET(EventsUrl.EVENTS_LIST)
    Observable<EventResponseEntity> getEvents();

    @GET(EventsUrl.EVENTS_LIST_SEARCH)
    Observable<EventResponseEntity> getSearchEvents(@QueryMap Map<String, Object> param);

    @GET(EventsUrl.EVENTS_LOCATION_LIST)
    Observable<EventLocationEntity> getEventsLocationList();

    @GET(EventsUrl.EVENTS_LIST_BY_LOCATION)
    Observable<EventResponseEntity> getEventsByLocation(@Path("location") String location);

    @GET()
    Observable<EventResponseEntity> getSearchEvents(@Url String url);

    @GET()
    Observable<EventsDetailsEntity> getEventDetails(@Url String url);

    @POST(EventsUrl.EVENTS_VERIFY)
    @Headers({"Content-Type: application/json"})
    Observable<VerifyCartResponse> postCartVerify(@Body JsonObject requestBody, @Query("book") String value);

    @POST(EventsUrl.EVENT_VALIDATE)
    @Headers({"Content-Type: application/json"})
    Observable<ValidateResponse> validateShow(@Body JsonObject requestBody);

    @POST(EventsUrl.EVENTS_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<CheckoutResponse> checkoutCart(@Body JsonObject requestBody);

    @GET()
    Observable<List<SeatLayoutItem>> getSeatLayout(@Url String url);


}
