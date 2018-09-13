package com.tokopedia.tkpdpdp.presenter.di;

import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {ApiModule.class}
)
public class ProductDetailModule {

    @Provides
    GetWishlistCountUseCase provideGetWishlistCountUseCase(
            @MojitoQualifier MojitoApi mojitoApi
    ){
        return new GetWishlistCountUseCase(mojitoApi);
    }
}
