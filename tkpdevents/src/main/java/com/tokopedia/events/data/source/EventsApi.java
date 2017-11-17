package com.tokopedia.events.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsApi {
    @GET(EventsUrl.EVENTS_LIST)
    Observable<EventResponseEntity> getEvents();

    @GET(EventsUrl.EVENTS_LIST_SEARCH)
    Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params);

    @GET(EventsUrl.EVENTS_LOCATION_LIST)
    Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params);
}
