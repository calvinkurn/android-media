package com.tokopedia.flight.search.data.cache;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by hendry on 7/14/2017.
 */

public class FlightSearchSingleDataCacheSource extends DataCacheSource {

    private static final String PREF_KEY_NAME = "FLIGHT_SEARCH_SINGLE";
    private static final long ONE_HOUR = TimeUnit.HOURS.toSeconds(1);

    @Inject
    public FlightSearchSingleDataCacheSource(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_HOUR;
    }
}
