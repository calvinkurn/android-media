package com.tokopedia.movies.data.source;

/**
 * Created by ashwanityagi on 02/11/17.
 */

public interface EventsUrl {

    String EVENTS_LIST = "v1/api/h/movie";
    String EVENTS_LIST_SEARCH = "v1/api/s/movie";
    String EVENTS_LOCATION_LIST = "v1/api/location";
    String EVENTS_LIST_BY_LOCATION = "v1/api/h/movie/{location}";
    String EVENTS_VERIFY = "/v1/api/expresscart/verify";
    String EVENTS_CHECKOUT = "/v1/api/expresscart/checkout";
    String EVENT_DETAIL = "v1/api/p";
    String EVENT_VALIDATE = "/v1/api/movie/validate-selection";
    String EVENT_SEAT_LAYOUT = "/v1/api/seat-layout/category/{category_id}/product/{product_id}/schedule/{schedule_id}/group/{group_id}/package/{package_id}";


}
