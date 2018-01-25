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


public interface EventDataStore {

    Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params);

    Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params);

    Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params);

    Observable<EventResponseEntity> getEventsListByLocation(String location);

    Observable<EventsDetailsEntity> getEventDetails(String url);

    Observable<ValidateResponse> validateShow(JsonObject requestBody);

    Observable<VerifyCartResponse> verifyCart(JsonObject requestBody);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);

    Observable<List<SeatLayoutItem>> getSeatLayout(String url);
}
