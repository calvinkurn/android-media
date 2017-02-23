package com.tokopedia.discovery.search.di;

import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.core.base.di.qualifier.AceQualifier;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author erry on 23/02/17.
 */

@Module
public class SearchModule {

    @SearchScope
    @Provides
    AceService provideAceService(@AceQualifier Retrofit retrofit){
        return retrofit.create(AceService.class);
    }
}
