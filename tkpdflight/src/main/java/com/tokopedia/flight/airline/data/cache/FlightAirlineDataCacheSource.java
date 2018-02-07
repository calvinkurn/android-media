package com.tokopedia.flight.airline.data.cache;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by hendry on 7/14/2017.
 */

public class FlightAirlineDataCacheSource extends DataCacheSource {

    private static final String PREF_KEY_NAME = "PREF_KEY_AIRLINE_LIST";
    private static final long ONE_MONTH = TimeUnit.DAYS.toSeconds(30);

    @Inject
    public FlightAirlineDataCacheSource(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_MONTH;
    }
}
