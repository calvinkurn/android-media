package com.tokopedia.events.data.source;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsUrl {

     String EVENTS_LIST = "v1/api/h/event";
     String EVENTS_LIST_SEARCH = "v1/api/s/event";
     String EVENTS_LOCATION_LIST = "v1/api/location";
     String EVENTS_LIST_BY_LOCATION = "v1/api/h/event/{location}";


}
