package com.tokopedia.tkpdpdp.presenter.di;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 10/6/17.
 */

@Module
public class ApiModule {
    @MojitoQualifier
    @Provides
    MojitoApi mojitoGetWishlistCount(@MojitoQualifier Retrofit retrofit){
        return retrofit.create(MojitoApi.class);
    }

    @TopAdsQualifier
    @Provides
    TopAdsService provideTopAdsService(@TopAdsQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsService.class);
    }
}
