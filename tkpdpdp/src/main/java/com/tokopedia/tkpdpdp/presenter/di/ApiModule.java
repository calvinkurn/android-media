package com.tokopedia.tkpdpdp.presenter.di;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 10/6/17.
 */

@Module
public class ApiModule {
    @MojitoGetWishlistQualifier
    @Provides
    MojitoApi mojitoGetWishlistCount(@MojitoGetWishlistQualifier Retrofit retrofit){
        return retrofit.create(MojitoApi.class);
    }
}
