package com.tokopedia.seller.shop.setting.di.component;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 3/17/17.
 */
@ShopSettingScope
@Component(modules = ShopSettingModule.class, dependencies = ShopComponent.class)
public interface ShopSettingComponent {

    DistrictLogisticDataRepository getDistrictLogisticDataRepository();

    ThreadExecutor getThreadExecutor();

    PostExecutionThread getPostExecutionThread();

    TomeApi getTomeApi();

    Retrofit.Builder retrofitBuilder();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @ApplicationContext
    Context context();

    @WsV4QualifierWithErrorHander
    Retrofit retrofitWsV4();
}
