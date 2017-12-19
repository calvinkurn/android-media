package com.tokopedia.events.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.moshi.Json;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.Cart;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;

import rx.Observable;

/**
 * Created by ashwanityagi on 07/11/17.
 */

public interface EventDataStore {

    Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params);

    Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params);

    Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params);

    Observable<EventResponseEntity> getEventsListByLocation(String location);

    Observable<EventsDetailsEntity> getEventDetails(String url);

    Observable<ValidateResponse> validateShow(JsonObject requestBody);

    Observable<VerifyCartResponse> verifyCart(JsonObject requestBody);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);



}
