package com.tokopedia.flight.search.util;

import com.tokopedia.usecase.RequestParams;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchParamUtil {

    public static final String PARAM_IS_RETURNING = "is_return";

    // TODO generate request param for search flight
    public static RequestParams generateRequestParams(boolean isReturning){
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning);
        return requestParams;
    }

    public static boolean isReturning(RequestParams requestParams){
        return requestParams.getBoolean(PARAM_IS_RETURNING, false);
    }
}
