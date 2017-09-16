package com.tokopedia.gm.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.gm.common.di.module.GoldMerchantModule;
import com.tokopedia.gm.common.di.scope.GoldMerchantScope;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@GoldMerchantScope
@Component(modules = GoldMerchantModule.class, dependencies = AppComponent.class)
public interface GoldMerchantComponent {
    void inject(BaseDatePickerFragment datePickerFragment);

    DatePickerPresenter datePickerPresenter();

    @ApplicationContext
    Context context();

    @TopAdsQualifier
    Retrofit topAdsRetrofit();

    @AceQualifier
    Retrofit aceRetrofit();

    @MerlinQualifier
    Retrofit merlinRetrofit();

    @MojitoQualifier
    Retrofit mojitoRetrofit();

    @HadesQualifier
    Retrofit hadesRetrofit();

    @YoutubeQualifier
    Retrofit youtubeRetrofit();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @ResolutionQualifier
    Retrofit resolutionRetrofit();

    @GoldMerchantQualifier
    Retrofit goldMerchantRetrofit();

    @CartQualifier
    Retrofit cartRetrofit();

    Retrofit.Builder retrofitBuilder();

    Gson gson();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();

    @WsV4QualifierWithErrorHander
    Retrofit baseDomainWithErrorHandlerRetrofit();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    SessionHandler sessionHandler();

    GCMHandler gcmHandler();

    ImageHandler imageHandler();

    ApiCacheRepository apiCacheRepository();

    CacheApiWhiteListUseCase cacheApiWhiteListUseCase();

    ApiCacheDataSource cacheHelper();
}
