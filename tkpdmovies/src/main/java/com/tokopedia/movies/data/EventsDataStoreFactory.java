package com.tokopedia.movies.data;

import com.tokopedia.movies.data.source.EventsApi;


public class EventsDataStoreFactory {
    private final EventsApi moviesApi;

    public EventsDataStoreFactory(EventsApi moviesApi){
        this.moviesApi=moviesApi;
    }

    public EventDataStore createCloudDataStore(){
        return new CloudEventsDataStore(moviesApi);
    }
}
