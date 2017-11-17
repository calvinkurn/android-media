package com.tokopedia.flight.search.util;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchMetaParamUtil {
    private static final String PARAM_DEPARTURE = "departure";
    private static final String PARAM_ARRIVAL = "arrival";
    private static final String PARAM_DEP_DATE = "dep_date";

    public static RequestParams generateRequestParams(String departureAirport, String arrivalAirport, String date) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_DEPARTURE, departureAirport);
        requestParams.putString(PARAM_ARRIVAL, arrivalAirport);
        requestParams.putString(PARAM_DEP_DATE, date);
        return requestParams;
    }

    public static String getDeparture(RequestParams requestParams) {
        return requestParams.getString(PARAM_DEPARTURE, "");
    }
    public static String getArrival(RequestParams requestParams) {
        return requestParams.getString(PARAM_ARRIVAL, "");
    }
    public static String getDate(RequestParams requestParams) {
        return requestParams.getString(PARAM_DEP_DATE, "");
    }

    public static HashMap<String, Object> toHashMap(RequestParams requestParams) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(PARAM_DEPARTURE, getDeparture(requestParams));
        hashMap.put(PARAM_ARRIVAL, getArrival(requestParams));
        hashMap.put(PARAM_DEP_DATE, getDate(requestParams));
        return hashMap;
    }

    public static String getDeparture(HashMap<String, Object> hashMap) {
        return (String) hashMap.get(PARAM_DEPARTURE);
    }

    public static String getArrival(HashMap<String, Object> hashMap) {
        return (String) hashMap.get(PARAM_ARRIVAL);
    }

    public static String getDate(HashMap<String, Object> hashMap) {
        return (String) hashMap.get(PARAM_DEP_DATE);
    }
}
