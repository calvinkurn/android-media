package com.tokopedia.events.data;

import com.tokopedia.events.data.source.EventsApi;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventsDataStoreFactory {
    private final EventsApi eventsApi;

    public EventsDataStoreFactory(EventsApi eventsApi){
        this.eventsApi=eventsApi;
    }

    public EventDataStore createCloudDataStore(){
        return new CloudEventsDataStore(eventsApi);
    }
}
