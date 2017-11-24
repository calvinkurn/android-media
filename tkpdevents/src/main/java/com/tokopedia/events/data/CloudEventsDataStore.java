package com.tokopedia.events.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;
import com.tokopedia.events.data.source.EventsApi;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class CloudEventsDataStore implements EventDataStore {

    private final EventsApi eventsApi;

    public CloudEventsDataStore(EventsApi eventsApi){
        this.eventsApi=eventsApi;
    }


    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return eventsApi.getEvents();
    }

    @Override
    public Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params) {
        return eventsApi.getSearchEvents(params);
    }

    @Override
    public Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return eventsApi.getEventsLocationList();
    }

    @Override
    public Observable<EventResponseEntity> getEventsListByLocation(String location) {
        return eventsApi.getEventsByLocation(location);
    }

    @Override
    public Observable<EventsDetailsEntity> getEventDetails(String url) {
        return null;
    }
}
