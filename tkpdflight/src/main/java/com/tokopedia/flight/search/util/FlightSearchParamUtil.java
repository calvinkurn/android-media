package com.tokopedia.flight.search.util;

import com.tokopedia.usecase.RequestParams;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchParamUtil {

    private static final String PARAM_IS_RETURNING = "is_return";
    private static final String PARAM_FROM_CACHE = "from_cache";

    // TODO generate request param for search flight
    public static RequestParams generateRequestParams(boolean isReturning, boolean fromCache){
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning);
        requestParams.putBoolean(PARAM_FROM_CACHE, fromCache);
        return requestParams;
    }

    public static boolean isReturning(RequestParams requestParams){
        return requestParams.getBoolean(PARAM_IS_RETURNING, false);
    }

    public static boolean isFromCache(RequestParams requestParams){
        return requestParams.getBoolean(PARAM_FROM_CACHE, false);
    }
}
