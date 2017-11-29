package com.tokopedia.events.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.EventsDetailsEntity;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class DiskEventsdataStore implements EventDataStore {

    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<EventResponseEntity> getSearchEvents(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<EventLocationEntity> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return null;
    }

    @Override
    public Observable<EventResponseEntity> getEventsListByLocation(String location) {
        return null;
    }

    @Override
    public Observable<EventsDetailsEntity> getEventDetails(String url) {
        return null;
    }
}
