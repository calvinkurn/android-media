package com.tokopedia.movies.data;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
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

import rx.Observable;


public class DiskEventsdataStore implements EventDataStore {

    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params) {
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
    public Observable<VerifyCartResponse> verifyCart(JsonObject requestBody) {
        return null;
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return null;
    }


    @Override
    public Observable<List<SeatLayoutItem>> getSeatLayout(String url) {
        return null;
    }
}
