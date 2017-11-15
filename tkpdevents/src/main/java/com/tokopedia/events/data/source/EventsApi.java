package com.tokopedia.events.data.source;

import com.tokopedia.events.data.entity.EventResponseEntity;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsApi {
    @GET(EventsUrl.EVENTS_LIST)
    Observable<EventResponseEntity> getEvents();
}
