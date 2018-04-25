package com.tokopedia.tokocash.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.accountsetting.data.AccountSettingRepository;
import com.tokopedia.tokocash.accountsetting.domain.GetOAuthInfoTokoCashUseCase;
import com.tokopedia.tokocash.accountsetting.domain.PostUnlinkTokoCashUseCase;
import com.tokopedia.tokocash.activation.data.ActivateRepository;
import com.tokopedia.tokocash.activation.domain.LinkedTokoCashUseCase;
import com.tokopedia.tokocash.activation.domain.RequestOtpTokoCashUseCase;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.GetReasonHelpDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.MoveToSaldoUseCase;
import com.tokopedia.tokocash.historytokocash.domain.PostHelpHistoryDetailUseCase;
import com.tokopedia.tokocash.network.WalletTokenRefresh;
import com.tokopedia.tokocash.network.api.TokoCashApi;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.network.api.WalletBalanceApi;
import com.tokopedia.tokocash.network.api.WalletUrl;
import com.tokopedia.tokocash.network.interceptor.TokoCashAuthInterceptor;
import com.tokopedia.tokocash.network.interceptor.TokoCashErrorResponseInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletAuthInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletErrorResponseInterceptor;
import com.tokopedia.tokocash.network.model.ActivateTokoCashErrorResponse;
import com.tokopedia.tokocash.network.model.TokoCashErrorResponse;
import com.tokopedia.tokocash.network.model.WalletErrorResponse;
import com.tokopedia.tokocash.pendingcashback.data.PendingCashbackRepository;
import com.tokopedia.tokocash.pendingcashback.domain.GetPendingCasbackUseCase;
import com.tokopedia.tokocash.qrpayment.data.repository.BalanceRepository;
import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nabillasabbaha on 12/27/17.
 */
@Module
public class TokoCashModule {

    public TokoCashModule() {

    }

    @Provides
    WalletUserSession provideTokoCashSession(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            return ((TokoCashRouter) context).getTokoCashSession();
        }
        return null;
    }

    @Provides
    public TokoCashRouter provideTokoCashRouter(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            return ((TokoCashRouter) context);
        }
        throw new RuntimeException("App should implement " + TokoCashRouter.class.getSimpleName());
    }

    @Provides
    @TokoCashChuckQualifier
    public Interceptor provideChuckInterceptor(TokoCashRouter tokoCashRouter) {
        return tokoCashRouter.getChuckInterceptor();
    }

    @Provides
    @OkHttpTokoCashQualifier
    OkHttpClient provideOkHttpClient(TokoCashAuthInterceptor tokoCashAuthInterceptor, Gson gson,
                                     @TokoCashChuckQualifier Interceptor chuckIntereptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(tokoCashAuthInterceptor)
                .addInterceptor(new TokoCashErrorResponseInterceptor(TokoCashErrorResponse.class, gson))
                .addInterceptor(new TokoCashErrorResponseInterceptor(ActivateTokoCashErrorResponse.class, gson))
                .addInterceptor(chuckIntereptor).build();
    }

    @Provides
    @RetrofitTokoCashQualifier
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, @OkHttpTokoCashQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(WalletUrl.BaseUrl.ACCOUNTS_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    TokoCashApi provideTokoCashApi(@RetrofitTokoCashQualifier Retrofit retrofit) {
        return retrofit.create(TokoCashApi.class);
    }

    @Provides
    WalletTokenRefresh provideWalletTokenRefresh(WalletUserSession walletUserSession, @RetrofitTokoCashQualifier Retrofit retrofit) {
        return new WalletTokenRefresh(walletUserSession, retrofit);
    }

    @Provides
    @OkHttpWalletQualifier
    OkHttpClient provideOkHttpClientWallet(WalletAuthInterceptor walletAuthInterceptor, Gson gson,
                                           WalletTokenRefresh walletTokenRefresh, WalletUserSession walletUserSession,
                                           @TokoCashChuckQualifier Interceptor chuckIntereptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(walletAuthInterceptor)
                .addInterceptor(new WalletErrorResponseInterceptor(WalletErrorResponse.class, gson,
                        walletTokenRefresh, walletUserSession))
                .addInterceptor(chuckIntereptor).build();
    }

    @Provides
    @RetrofitWalletQualifier
    Retrofit provideRetrofitWallet(Retrofit.Builder retrofitBuilder,
                                   @OkHttpWalletQualifier OkHttpClient okHttpClient, Gson gson) {
        return retrofitBuilder.baseUrl(WalletUrl.BaseUrl.WALLET_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    WalletApi provideWalletApi(@RetrofitWalletQualifier Retrofit retrofit) {
        return retrofit.create(WalletApi.class);
    }

    @Provides
    GetHistoryDataUseCase provideGetHistoryDataUseCase(WalletRepository walletRepository) {
        return new GetHistoryDataUseCase(walletRepository);
    }

    @Provides
    GetReasonHelpDataUseCase provideGetReasonHelpDataUseCase(WalletRepository walletRepository) {
        return new GetReasonHelpDataUseCase(walletRepository);
    }

    @Provides
    GetInfoQrTokoCashUseCase provideGetInfoQrTokoCashUseCase(QrPaymentRepository qrPaymentRepository) {
        return new GetInfoQrTokoCashUseCase(qrPaymentRepository);
    }

    @Provides
    PostQrPaymentUseCase providePostQrPaymentUseCase(QrPaymentRepository qrPaymentRepository) {
        return new PostQrPaymentUseCase(qrPaymentRepository);
    }

    @Provides
    GetBalanceTokoCashUseCase provideGetBalanceTokoCashUseCase(BalanceRepository tokoCashBalanceRepository) {
        return new GetBalanceTokoCashUseCase(tokoCashBalanceRepository);
    }

    @Provides
    @TokoCashScope
    LinkedTokoCashUseCase provideActivateTokoCashUseCase(ActivateRepository activateRepository) {
        return new LinkedTokoCashUseCase(activateRepository);
    }

    @Provides
    @TokoCashScope
    RequestOtpTokoCashUseCase provideRequestOtpTokoCashUseCase(ActivateRepository activateRepository) {
        return new RequestOtpTokoCashUseCase(activateRepository);
    }

    @Provides
    @TokoCashScope
    GetPendingCasbackUseCase provideGetPendingCasbackUseCase(PendingCashbackRepository pendingCashbackRepository) {
        return new GetPendingCasbackUseCase(pendingCashbackRepository);
    }

    @Provides
    @TokoCashScope
    PostHelpHistoryDetailUseCase providePostHelpHistoryDetailUseCase(WalletRepository walletRepository) {
        return new PostHelpHistoryDetailUseCase(walletRepository);
    }

    @Provides
    @TokoCashScope
    MoveToSaldoUseCase provideMoveToSaldoUseCase(WalletRepository walletRepository) {
        return new MoveToSaldoUseCase(walletRepository);
    }

    @Provides
    @TokoCashScope
    GetOAuthInfoTokoCashUseCase provideGetOAuthInfoTokoCashUseCase(AccountSettingRepository accountSettingRepository) {
        return new GetOAuthInfoTokoCashUseCase(accountSettingRepository);
    }

    @Provides
    @TokoCashScope
    PostUnlinkTokoCashUseCase providePostUnlinkTokoCashUseCase(AccountSettingRepository accountSettingRepository) {
        return new PostUnlinkTokoCashUseCase(accountSettingRepository);
    }

    @Provides
    WalletBalanceApi provideWalletBalanceApi() {
        Retrofit retrofit = RetrofitFactory.createRetrofitDefaultConfig(TkpdBaseURL.HOME_DATA_BASE_URL)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                        .buildClientDefaultAuth())
                .build();

        return retrofit.create(WalletBalanceApi.class);
    }

    @Provides
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }

}
