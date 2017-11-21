package com.tokopedia.events.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventLocationDomain;

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

}
