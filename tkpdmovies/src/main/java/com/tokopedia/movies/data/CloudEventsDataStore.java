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
import com.tokopedia.movies.data.source.EventsApi;

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class CloudEventsDataStore implements EventDataStore {

    private final EventsApi moviesApi;

    public CloudEventsDataStore(EventsApi moviesApi){
        this.moviesApi=moviesApi;
    }


    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return moviesApi.getEvents();
    }

    @Override
    public Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params) {
        return moviesApi.getSearchEvents(params);
    }

    @Override
    public Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return moviesApi.getEventsLocationList();
    }

    @Override
    public Observable<EventResponseEntity> getEventsListByLocation(String location) {
        return moviesApi.getEventsByLocation(location);
    }

    @Override
    public Observable<EventsDetailsEntity> getEventDetails(String url) {
        return moviesApi.getEventDetails(url);
    }

    @Override
    public Observable<ValidateResponse> validateShow(JsonObject requestBody) {
        return moviesApi.validateShow(requestBody);
    }

    @Override
    public Observable<VerifyCartResponse> verifyCart(JsonObject requestBody) {
        return moviesApi.postCartVerify(requestBody,"true");
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return moviesApi.checkoutCart(requestBody);
    }


    @Override
    public Observable<List<SeatLayoutItem>> getSeatLayout(String url) {
        return moviesApi.getSeatLayout(url);
    }
}
