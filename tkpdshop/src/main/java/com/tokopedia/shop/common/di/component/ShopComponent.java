package com.tokopedia.shop.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.common.di.module.ShopModule;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Component(modules = ShopModule.class, dependencies = BaseAppComponent.class)
public interface ShopComponent {

    @ApplicationContext
    Context getContext();

    CacheApiInterceptor cacheApiInterceptor();

    ShopApi getShopApi();

    ShopWSApi ShopWSApi();

    UserSession getUserSession();

    AbstractionRouter getAbstractionRouter();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    TkpdAuthInterceptor tkpdAuthInterceptor();

    HeaderErrorResponseInterceptor headerErrorResponseInterceptor();

    HttpLoggingInterceptor httpLoggingInterceptor();

    GetShopInfoUseCase getShopInfoUseCase();

    GetShopInfoByDomainUseCase getShopInfoByDomainUseCase();

    ToggleFavouriteShopUseCase toggleFavouriteShopUseCase();
}