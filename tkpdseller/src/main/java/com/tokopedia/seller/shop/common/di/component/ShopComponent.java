package com.tokopedia.seller.shop.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.manageitem.domain.repository.ShopInfoRepository;
import com.tokopedia.seller.manageitem.domain.usecase.GetShopInfoUseCase;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.seller.shop.common.di.module.ShopModule;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 10/20/17.
 */
@ShopScope
@Component(modules = ShopModule.class, dependencies = AppComponent.class)
public interface ShopComponent {

    @ApplicationContext
    Context context();

    Gson gson();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    @WsV4QualifierWithErrorHander
    Retrofit retrofitWsV4();

    Retrofit.Builder getRetrofitBuilder();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();

    @ShopQualifier
    Retrofit shopRetrofit();

    ShopInfoRepository shopInfoRepository();

    GlobalCacheManager globalCacheManager();

    GetShopInfoUseCase getShopInfoUseCase();
}
