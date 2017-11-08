package com.tokopedia.events.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.EventResponseEntity;
import com.tokopedia.events.data.source.EventsApi;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class CloudEventsDataStore implements EventDataStore{

    private final EventsApi eventsApi;

    public CloudEventsDataStore(EventsApi eventsApi){
        this.eventsApi=eventsApi;
    }


    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return eventsApi.getEvents(params);
    }



}
