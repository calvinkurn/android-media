package com.tokopedia.gm.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.gm.cashback.domain.GetCashbackUseCase;
import com.tokopedia.gm.cashback.domain.SetCashbackUseCase;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductGetListUseCase;
import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.gm.common.di.module.GMModule;
import com.tokopedia.gm.common.di.scope.GMScope;
import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@GMScope
@Component(modules = GMModule.class, dependencies = AppComponent.class)
public interface GMComponent {
    void inject(BaseDatePickerFragment datePickerFragment);

    DatePickerPresenter datePickerPresenter();

    @ApplicationContext
    Context context();

    @AceQualifier
    Retrofit aceRetrofit();

    @MerlinQualifier
    Retrofit merlinRetrofit();

    @MojitoQualifier
    Retrofit mojitoRetrofit();

    @HadesQualifier
    Retrofit hadesRetrofit();

    @DefaultAuthWithErrorHandler
    OkHttpClient okHttpClient();

    @GoldMerchantQualifier
    Retrofit goldMerchantRetrofit();

    @CartQualifier
    Retrofit cartRetrofit();

    @TomeQualifier
    Retrofit tomeQualifier();

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

    GMFeaturedProductGetListUseCase getFeaturedProductGetListUseCase();

    SetCashbackUseCase getSetCashbackUseCase();

    GetCashbackUseCase getCashbackUseCase();
}
