package com.tokopedia.movies.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.ValidateResponse;
import com.tokopedia.movies.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.movies.domain.model.EventsCategoryDomain;
import com.tokopedia.movies.domain.model.EventLocationDomain;
import com.tokopedia.movies.domain.model.EventDetailsDomain;

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public interface EventRepository {

    Observable<List<EventsCategoryDomain>> getEvents(TKPDMapParam<String, Object> params);

    Observable<List<EventsCategoryDomain>> getSearchEvents(TKPDMapParam<String, Object> params);

    Observable<List<EventLocationDomain>> getEventsLocationList(TKPDMapParam<String, Object> params);

    Observable<List<EventsCategoryDomain>> getEventsListByLocation(String location);

    Observable<EventDetailsDomain> getEventDetails(String url);

    Observable<ValidateResponse> validateShow(JsonObject requestBody);

    Observable<VerifyCartResponse> verifyCard(JsonObject requestBody);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);

    Observable<List<SeatLayoutItem>> getSeatLayout(String url);
}
