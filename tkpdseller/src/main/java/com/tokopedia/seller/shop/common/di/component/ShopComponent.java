package com.tokopedia.seller.shop.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.shop.common.di.ShopScope;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;

import dagger.Component;
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

    @TomeQualifier
    Retrofit tomeRetrofit();

    TomeApi tomeApi();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();
}
