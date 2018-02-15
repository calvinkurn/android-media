package com.tokopedia.flight.search.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

@FlightSearchScope
@Module
public class FlightSearchModule {

    public FlightSearchModule() {
    }

    @FlightSearchQualifier
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        String key = "FLIGHT_SEARCH_CACHE";
        return new LocalCacheHandler(context, key);
    }
}
