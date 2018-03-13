package com.tokopedia.tkpdtrain.search.data;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;

import java.util.concurrent.TimeUnit;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleCacheDataStore extends DataCacheSource {

    private static final String PREF_KEY_NAME = "PREF_KEY_TRAIN_SEARCH";
    private static final long ONE_HOURS = TimeUnit.HOURS.toSeconds(1);

    public TrainScheduleCacheDataStore(Context context) {
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
