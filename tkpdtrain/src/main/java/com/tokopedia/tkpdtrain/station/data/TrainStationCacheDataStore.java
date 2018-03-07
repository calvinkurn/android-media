package com.tokopedia.tkpdtrain.station.data;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;

import java.util.concurrent.TimeUnit;

/**
 * @author  by alvarisi on 3/5/18.
 */

public class TrainStationCacheDataStore extends DataCacheSource {
    private static final String PREF_KEY_NAME = "PREF_KEY_TRAIN_STATIONS";
    private static final long ONE_HOURS = TimeUnit.HOURS.toSeconds(1);

    public TrainStationCacheDataStore(Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_HOURS;
    }
}
