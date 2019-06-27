package com.tokopedia.discovery.similarsearch.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.newdiscovery.di.module.ProductModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

@Module(includes = {ProductModule.class, ApiModule.class})
public class SimilarSearchModule {
    @ApplicationContext
    @Provides
    Context context(@com.tokopedia.core.base.di.qualifier.ApplicationContext Context context) {
        return context;
    }
}
