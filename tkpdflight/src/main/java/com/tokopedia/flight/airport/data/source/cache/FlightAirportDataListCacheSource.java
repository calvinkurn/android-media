package com.tokopedia.flight.airport.data.source.cache;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by hendry on 7/14/2017.
 */

public class FlightAirportDataListCacheSource extends DataListCacheSource{

    private static final String PREF_KEY_NAME = "PREF_KEY_DISTRICT_LIST";
    private static final long ONE_WEEK = TimeUnit.DAYS.toSeconds(7);

    @Inject
    public FlightAirportDataListCacheSource(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_WEEK;
    }
}
