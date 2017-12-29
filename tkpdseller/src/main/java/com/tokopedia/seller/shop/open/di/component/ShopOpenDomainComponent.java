package com.tokopedia.seller.shop.open.di.component;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenDomainFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLocationFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenMandatoryLogisticFragment;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenRoutingFragment;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingInfoModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenter;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by sebastianuskh on 3/17/17.
 */
@ShopOpenDomainScope
@Component(modules = {ShopOpenDomainModule.class, ShopSettingInfoModule.class}, dependencies = ShopComponent.class)
public interface ShopOpenDomainComponent {

	void inject(ShopOpenRoutingFragment shopOpenRoutingFragment);

    void inject(ShopOpenDomainFragment shopOpenDomainFragment);

    void inject(ShopOpenMandatoryLocationFragment shopOpenMandatoryLocationFragment);

    void inject(ShopOpenMandatoryLogisticFragment shopOpenMandatoryLogisticFragment);

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

    ShopOpenRepository provideShopOpenRepository();

    ShopSettingInfoPresenter shopSettingInfoPresenter();

    NetworkCalculator networkCalculator();
}
