package com.tokopedia.events.data.source;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsUrl {

    String EVENTS_LIST = "v1/api/h/event";
    String EVENTS_LIST_SEARCH = "v1/api/s/event";
    String EVENTS_LOCATION_LIST = "v1/api/location";
    String EVENTS_LIST_BY_LOCATION = "v1/api/h/event/{location}";
    String EVENTS_VERIFY = "/v1/api/expresscart/verify";
    String EVENTS_CHECKOUT = "/v1/api/expresscart/checkout";
    String EVENT_DETAIL = "v1/api/p/";
    String EVENT_VALIDATE = "/v1/api/event/validate-selection";
    String EVENT_SEAT_LAYOUT = "/v1/api/seat-layout/category/{category_id}/product/{product_id}/schedule/{schedule_id}/group/{group_id}/package/{package_id}";


}
