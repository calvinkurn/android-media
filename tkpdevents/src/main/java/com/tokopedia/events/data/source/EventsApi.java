package com.tokopedia.events.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;

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

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsApi {
    @GET(EventsUrl.EVENTS_LIST)
    Observable<EventResponseEntity> getEvents();

    @GET(EventsUrl.EVENTS_LIST_SEARCH)
    Observable<SearchResponse> getSearchEvents(@QueryMap Map<String, Object> param);

    @GET(EventsUrl.EVENTS_LOCATION_LIST)
    Observable<EventLocationEntity> getEventsLocationList();

    @GET(EventsUrl.EVENTS_LIST_BY_LOCATION)
    Observable<EventResponseEntity> getEventsByLocation(@Path("location") String location);

    @GET()
    Observable<EventsDetailsEntity> getEventDetails(@Url String url);

    @GET()
    Observable<SearchResponse> getSearchNext(@Url String nextUrl);

    @POST(EventsUrl.EVENTS_VERIFY)
    @Headers({"Content-Type: application/json"})
    Observable<VerifyCartResponse> postCartVerify(@Body JsonObject requestBody, @Query("book") boolean value);

    @POST(EventsUrl.EVENT_VALIDATE)
    @Headers({"Content-Type: application/json"})
    Observable<ValidateResponse> validateShow(@Body JsonObject requestBody);

    @POST(EventsUrl.EVENTS_CHECKOUT)
    @Headers({"Content-Type: application/json"})
    Observable<CheckoutResponse> checkoutCart(@Body JsonObject requestBody);

    @GET(EventsUrl.EVENT_SEAT_LAYOUT)
    Observable<SeatLayoutResponse> getSeatLayout(@Path("category_id") int category_id,
                                                 @Path("product_id") int product_id,
                                                 @Path("schedule_id") int schedule_id,
                                                 @Path("group_id") int group_id,
                                                 @Path("package_id") int package_id);

    @GET()
    Observable<List<SeatLayoutItem>> getEventSeatLayout(@Url String url);


}
