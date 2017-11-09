package com.tokopedia.flight.search.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.db.AbsFlightSearchDataListDBSource;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class AbsFlightSearchDataListSource extends DataListSource<FlightSearchData, FlightSearchSingleRouteDB> {

    public AbsFlightSearchDataListSource(DataListCacheSource dataListCacheManager,
                                         AbsFlightSearchDataListDBSource absFlightSearchDataListDBSource,
                                         DataListCloudSource<FlightSearchData> dataListCloudManager) {
        super(dataListCacheManager, absFlightSearchDataListDBSource, dataListCloudManager);
    }

    public Observable<List<FlightSearchSingleRouteDB>> getDataList(final RequestParams requestParams) {
        if (FlightSearchParamUtil.isFromCache(requestParams)) {
            return super.getCacheDataList(FlightSearchParamUtil.toHashMap(requestParams));
        } else {
            return super.setCacheExpired().flatMap(new Func1<Boolean, Observable<List<FlightSearchSingleRouteDB>>>() {
                @Override
                public Observable<List<FlightSearchSingleRouteDB>> call(Boolean aBoolean) {
                    return AbsFlightSearchDataListSource.super.getDataList(FlightSearchParamUtil.toHashMap(requestParams));
                }
            });
        }
    }

    public Observable<List<FlightSearchSingleRouteDB>> getDataListCount(RequestParams requestParams) {
        if (FlightSearchParamUtil.isFromCache(requestParams)) {
            return super.getCacheDataList(FlightSearchParamUtil.toHashMap(requestParams));
        } else {
            return super.getDataList(FlightSearchParamUtil.toHashMap(requestParams));
        }
    }
}
