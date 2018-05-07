package com.tokopedia.flight.search.data;

import com.tokopedia.abstraction.base.data.source.DataSource;
import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataCloudSource;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.db.AbsFlightSearchDataDBSource;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class AbsFlightSearchDataSource extends DataSource<FlightDataResponse<List<FlightSearchData>>, List<FlightSearchSingleRouteDB>> {
    private AbsFlightSearchDataDBSource absFlightSearchDataDBSource;

    public AbsFlightSearchDataSource(DataCacheSource dataListCacheManager,
                                     AbsFlightSearchDataDBSource absFlightSearchDataDBSource,
                                     DataCloudSource<FlightDataResponse<List<FlightSearchData>>> dataCloudManager) {
        super(dataListCacheManager, absFlightSearchDataDBSource, dataCloudManager);
        this.absFlightSearchDataDBSource = absFlightSearchDataDBSource;
    }

    public Observable<List<FlightSearchSingleRouteDB>> getDataList(final RequestParams requestParams) {
        if (FlightSearchParamUtil.isFromCache(requestParams)) {
            return super.getCacheData(FlightSearchParamUtil.toHashMap(requestParams));
        } else {
            return super.setCacheExpired().flatMap(new Func1<Boolean, Observable<List<FlightSearchSingleRouteDB>>>() {
                @Override
                public Observable<List<FlightSearchSingleRouteDB>> call(Boolean aBoolean) {
                    return AbsFlightSearchDataSource.super.getCloudData(FlightSearchParamUtil.toHashMap(requestParams));
                }
            });
        }
    }

    public Observable<Integer> getCacheDataListCount(final HashMap<String, Object> params) {
        return absFlightSearchDataDBSource.getDataCount(params);
    }

    public Observable<FlightSearchSingleRouteDB> getSingleFlight(String id) {
        return absFlightSearchDataDBSource.find(id);
    }

    public Observable<Boolean> isDataAvailable() {
        return absFlightSearchDataDBSource.isDataAvailable();
    }
}
