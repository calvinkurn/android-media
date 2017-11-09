package com.tokopedia.flight.search.util;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchParamUtil {
    private static final String PARAM_INITIAL_SEARCH = "initial_search";
    private static final String PARAM_IS_RETURNING = "is_return";
    private static final String PARAM_FROM_CACHE = "from_cache";
    private static final String PARAM_FILTER_MODEL = "filter_model";
    private static final String PARAM_SORT = "param_sort";

    public static RequestParams generateRequestParams(FlightSearchPassDataViewModel flightSearchPassDataViewModel,
                                                      boolean isReturning, boolean fromCache, FlightFilterModel flightFilterModel,
                                                      @FlightSortOption int sortOptionId){
        RequestParams requestParams = RequestParams.create();
        if (flightSearchPassDataViewModel !=null) {
            requestParams.putObject(PARAM_INITIAL_SEARCH, flightSearchPassDataViewModel);
        }
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning);
        requestParams.putBoolean(PARAM_FROM_CACHE, fromCache);
        requestParams.putObject(PARAM_FILTER_MODEL, flightFilterModel);
        requestParams.putInt(PARAM_SORT, sortOptionId);
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

    public static int getSortOptionId(RequestParams requestParams){
        return requestParams.getInt(PARAM_SORT, FlightSortOption.NO_PREFERENCE);
    }

    public static FlightSearchPassDataViewModel getInitialPassData(RequestParams requestParams){
        return (FlightSearchPassDataViewModel) requestParams.getObject(PARAM_INITIAL_SEARCH);
    }

    public static HashMap<String, Object> toHashMap(RequestParams requestParams){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(PARAM_INITIAL_SEARCH, requestParams.getObject(PARAM_INITIAL_SEARCH));
        hashMap.put(PARAM_IS_RETURNING, requestParams.getObject(PARAM_IS_RETURNING));
        hashMap.put(PARAM_FILTER_MODEL, requestParams.getObject(PARAM_FILTER_MODEL));
        return hashMap;
    }

    public static FlightFilterModel getFilterModel(HashMap<String, Object> hashMap){
        return (FlightFilterModel) hashMap.get(PARAM_FILTER_MODEL);
    }

    public static boolean isReturning(HashMap<String, Object> hashMap){
        return (boolean) hashMap.get(PARAM_IS_RETURNING);
    }

    public static FlightSearchPassDataViewModel getInitialPassData(HashMap<String, Object> hashMap){
        return (FlightSearchPassDataViewModel) hashMap.get(PARAM_INITIAL_SEARCH);
    }
}
