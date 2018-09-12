package com.tokopedia.tkpdpdp.presenter.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoWishlistCountQualifier;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;
import com.tokopedia.tkpdpdp.presenter.di.scope.ProductDetailScope;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {ApiModule.class}
)
public class ProductDetailModule {

    @Provides
    GetWishlistCountUseCase provideGetWishlistCountUseCase(
            @MojitoWishlistCountQualifier MojitoApi mojitoApi
    ){
        return new GetWishlistCountUseCase(mojitoApi);
    }
}
