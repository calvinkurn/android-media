package com.tokopedia.flight.search.di;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.search.data.cache.FlightSearchSingleDataListCacheSource;
import com.tokopedia.flight.search.data.cloud.FlightSearchDataListCloudSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

@FlightSearchScope
@Module
public class FlightSearchModule {

}
