package com.tokopedia.seller.shop.open.di.component;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.shop.common.di.ShopQualifier;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.di.scope.ShopSettingInfoScope;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryInfoFragment;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 3/23/17.
 */
@ShopSettingInfoScope
@Component(dependencies = ShopOpenDomainComponent.class)
public interface ShopSettingInfoComponent {
    void inject(ShopOpenMandatoryInfoFragment shopOpenInfoFragment);

    @ShopQualifier
    TomeApi getTomeApi();

    Retrofit.Builder retrofitBuilder();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @ApplicationContext
    Context context();

    @WsV4QualifierWithErrorHander
    Retrofit retrofitWsV4();

    ShopOpenRepository provideShopOpenRepository();
}
