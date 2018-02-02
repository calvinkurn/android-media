package com.tokopedia.events.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventLocationDomain;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public interface EventRepository {

    Observable<List<EventsCategoryDomain>> getEvents(TKPDMapParam<String, Object> params);

    Observable<SearchDomainModel> getSearchEvents(TKPDMapParam<String, Object> params);

    Observable<SearchDomainModel> getSearchNext(String nextUrl);

    Observable<List<EventLocationDomain>> getEventsLocationList(TKPDMapParam<String, Object> params);

    Observable<List<EventsCategoryDomain>> getEventsListByLocation(String location);

    Observable<EventDetailsDomain> getEventDetails(String url);

    Observable<ValidateResponse> validateShow(JsonObject requestBody);

    Observable<VerifyCartResponse> verifyCard(JsonObject requestBody, boolean flag);

    Observable<CheckoutResponse> checkoutCart(JsonObject requestBody);

    Observable<SeatLayoutResponse> getSeatLayout(int category_id,
                                                 int product_id,
                                                 int schedule_id,
                                                 int group_id,
                                                 int package_id);
    Observable<List<SeatLayoutItem>> getEventSeatLayout(String url);

}
