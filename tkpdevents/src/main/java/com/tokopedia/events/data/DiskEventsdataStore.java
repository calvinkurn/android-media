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

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class DiskEventsdataStore implements EventDataStore {

    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<SearchResponse> getSearchEvents(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<SearchResponse> getSearchNext(String nextUrl) {
        return null;
    }

    @Override
    public Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<EventResponseEntity> getEventsListByLocation(String location) {
        return null;
    }

    @Override
    public Observable<EventsDetailsEntity> getEventDetails(String url) {
        return null;
    }

    @Override
    public Observable<ValidateResponse> validateShow(JsonObject requestBody) {
        return null;
    }

    @Override
    public Observable<VerifyCartResponse> verifyCart(JsonObject requestBody, boolean flag) {
        return null;
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return null;
    }

    @Override
    public Observable<SeatLayoutResponse> getSeatLayout(int category_id,
                                                        int product_id,
                                                        int schedule_id,
                                                        int group_id,
                                                        int package_id) {
        return null;
    }

    @Override
    public Observable<List<SeatLayoutItem>> getEventSeatLayout(String url) {
        return null;
    }


}
