package com.tokopedia.events.data;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.data.source.EventsApi;

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class CloudEventsDataStore implements EventDataStore {

    private final EventsApi eventsApi;

    public CloudEventsDataStore(EventsApi eventsApi){
        this.eventsApi=eventsApi;
    }


    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return eventsApi.getEvents();
    }

    @Override
    public Observable<SearchResponse> getSearchEvents(TKPDMapParam<String, Object> params) {
        return eventsApi.getSearchEvents(params);
    }

    @Override
    public Observable<SearchResponse> getSearchNext(String nextUrl) {
        return eventsApi.getSearchNext(nextUrl);
    }

    @Override
    public Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return eventsApi.getEventsLocationList();
    }

    @Override
    public Observable<EventResponseEntity> getEventsListByLocation(String location) {
        return eventsApi.getEventsByLocation(location);
    }

    @Override
    public Observable<EventsDetailsEntity> getEventDetails(String url) {
        return eventsApi.getEventDetails(url);
    }

    @Override
    public Observable<ValidateResponse> validateShow(JsonObject requestBody) {
        return eventsApi.validateShow(requestBody);
    }

    @Override
    public Observable<VerifyCartResponse> verifyCart(JsonObject requestBody, boolean flag) {
        return eventsApi.postCartVerify(requestBody,flag);
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return eventsApi.checkoutCart(requestBody);
    }

    @Override
    public Observable<SeatLayoutResponse> getSeatLayout(int category_id,
                                                        int product_id,
                                                        int schedule_id,
                                                        int group_id,
                                                        int package_id) {
        return eventsApi.getSeatLayout(category_id,product_id,schedule_id,group_id,package_id);
    }

    @Override
    public Observable<List<SeatLayoutItem>> getEventSeatLayout(String url) {
        return eventsApi.getEventSeatLayout(url);
    }


}
