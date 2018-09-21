package com.tokopedia.tkpdpdp.presenter.di;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;

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
}
