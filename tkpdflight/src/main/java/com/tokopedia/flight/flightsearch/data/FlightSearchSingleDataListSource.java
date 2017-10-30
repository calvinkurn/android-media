package com.tokopedia.flight.flightsearch.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.flightsearch.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.flightsearch.data.db.AbsFlightSearchDataListDBSource;
import com.tokopedia.flight.flightsearch.data.db.FlightSearchSingleDataListDBSource;
import com.tokopedia.flight.flightsearch.data.db.model.FlightSearchSingleRouteDB;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchSingleDataListSource extends AbsFlightSearchDataListSource {

    @Inject
    public FlightSearchSingleDataListSource(DataListCacheSource dataListCacheManager,
                                            FlightSearchSingleDataListDBSource flightSearchSingleDataListDBSource,
                                            DataListCloudSource<FlightSearchData> dataListCloudManager) {
        super(dataListCacheManager, flightSearchSingleDataListDBSource, dataListCloudManager);
    }
}
