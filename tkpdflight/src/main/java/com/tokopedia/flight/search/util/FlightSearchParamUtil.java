package com.tokopedia.flight.search.util;

import com.tokopedia.flight.search.view.model.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchParamUtil {

    private static final String PARAM_IS_RETURNING = "is_return";
    private static final String PARAM_FROM_CACHE = "from_cache";
    private static final String PARAM_FILTER_MODEL = "filter_model";

    // TODO generate request param for search flight
    public static RequestParams generateRequestParams(boolean isReturning, boolean fromCache, FlightFilterModel flightFilterModel){
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning);
        requestParams.putBoolean(PARAM_FROM_CACHE, fromCache);
        requestParams.putObject(PARAM_FILTER_MODEL, flightFilterModel);
        return requestParams;
    }

    public static boolean isReturning(RequestParams requestParams){
        return requestParams.getBoolean(PARAM_IS_RETURNING, false);
    }

    public static boolean isFromCache(RequestParams requestParams){
        return requestParams.getBoolean(PARAM_FROM_CACHE, false);
    }

    public static FlightFilterModel getFilterModel(RequestParams requestParams){
        return (FlightFilterModel) requestParams.getObject(PARAM_FILTER_MODEL);
    }
    public static FlightFilterModel getFilterModel(HashMap<String, Object> hashMap){
        return (FlightFilterModel) hashMap.get(PARAM_FILTER_MODEL);
    }

    public static HashMap<String, Object> toHashMap(RequestParams requestParams){
        HashMap<String, Object> hashMap = new HashMap<>();
        FlightFilterModel flightFilterModel = (FlightFilterModel) requestParams.getObject(PARAM_FILTER_MODEL);
        hashMap.put(PARAM_FILTER_MODEL, flightFilterModel);
        return hashMap;
    }
}
