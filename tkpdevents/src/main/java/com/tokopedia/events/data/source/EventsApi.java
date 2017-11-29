package com.tokopedia.events.data.source;

import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsApi {
    @GET(EventsUrl.EVENTS_LIST)
    Observable<EventResponseEntity> getEvents();

    @GET(EventsUrl.EVENTS_LIST_SEARCH)
    Observable<EventResponseEntity> getSearchEvents(@QueryMap Map<String, Object> param);

    @GET(EventsUrl.EVENTS_LOCATION_LIST)
    Observable<EventLocationEntity> getEventsLocationList();

    @GET(EventsUrl.EVENTS_LIST_BY_LOCATION)
    Observable<EventResponseEntity> getEventsByLocation(@Path("location") String location);

    @GET()
    Observable<EventResponseEntity> getSearchEvents(@Url String url);
}
