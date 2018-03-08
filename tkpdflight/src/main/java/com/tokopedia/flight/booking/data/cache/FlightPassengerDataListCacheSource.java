package com.tokopedia.flight.booking.data.cache;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by furqan on 01/03/18.
 */

public class FlightPassengerDataListCacheSource extends DataCacheSource {

    private static final String PREF_KEY_NAME = "PREF_KEY_NAME";
    private static final long THIRTY_MINUTES = TimeUnit.MINUTES.toSeconds(20);

    @Inject
    public FlightPassengerDataListCacheSource(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return THIRTY_MINUTES;
    }
}
