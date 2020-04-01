package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.network.service.AccountsBasicService;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.network.service.RegisterPhoneNumberApi;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.changename.data.mapper.ChangeNameMapper;
import com.tokopedia.session.changename.data.source.ChangeNameSource;
import com.tokopedia.session.changename.domain.usecase.ChangeNameUseCase;
import com.tokopedia.session.data.source.CreatePasswordDataSource;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.data.source.MakeLoginDataSource;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.register.data.mapper.CreatePasswordMapper;
import com.tokopedia.session.register.registerphonenumber.data.mapper.RegisterPhoneNumberMapper;
import com.tokopedia.session.register.registerphonenumber.data.source.CloudRegisterPhoneNumberSource;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.LoginRegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.RegisterPhoneNumberUseCase;
import com.tokopedia.session.register.view.util.AccountsAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

import static com.tokopedia.authentication.AuthHelper.getUserAgent;

/**
 * @author by nisie on 10/10/17.
 */

@Module
public class SessionModule {

    public static final String HMAC_SERVICE = "HMAC_SERVICE";
    public static final String BEARER_SERVICE = "BEARER_SERVICE";
    private static final String WS_SERVICE = "WS_SERVICE";
    public static final String LOGIN_CACHE = "LOGIN_CACHE";

    @SessionScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    /**
     * @return https://accounts.tokopedia.com
     * with Authorization : Tkpd
     */
    @SessionScope
    @Named(HMAC_SERVICE)
    @Provides
    AccountsService provideHMACAccountsService() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        return new AccountsService(bundle);
    }

    /**
     * @param context
     * @param sessionHandler
     * @return https://accounts.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @SessionScope
    @Named(BEARER_SERVICE)
    @Provides
    AccountsService provideBearerAccountsService(@ApplicationContext Context context,
                                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(sessionHandler.getAccessToken(context)))
            authKey = sessionHandler.getTokenType(context) + " " + sessionHandler
                    .getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }


    @SessionScope
    @Provides
    AccountsService provideAccountsService() {
        Bundle bundle = new Bundle();
        return new AccountsService(bundle);
    }


    /**
     * @param context
     * @param sessionHandler
     * @return https://ws.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @SessionScope
    @Named(WS_SERVICE)
    @Provides
    AccountsService provideWsAccountsService(@ApplicationContext Context context,
                                             SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + sessionHandler.getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        return new AccountsService(bundle);
    }

    @SessionScope
    @Provides
    AccountsAuthInterceptor provideAccountsAuthInterceptor() {
        return new AccountsAuthInterceptor();
    }

    @SessionScope
    @Provides
    TkpdAuthInterceptor provideTkpdAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }


    @SessionScope
    @Provides
    OkHttpClient provideRegisterOkHttpClient(TkpdAuthInterceptor authInterceptor, AccountsAuthInterceptor accountsAuthInterceptor, ChuckerInterceptor chuckInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(accountsAuthInterceptor)
                .addInterceptor(chuckInterceptor)
                .addInterceptor(chain -> {
                    Request.Builder newRequest = chain.request().newBuilder();
                    newRequest.addHeader("User-Agent", getUserAgent());
                    return chain.proceed(newRequest.build());
                })
                .addInterceptor(new AkamaiBotInterceptor())
                .build();
    }

    @SessionScope
    @Provides
    GetTokenDataSource provideGetTokenDataSource(AccountsBasicService
                                                         accountsService,
                                                 TokenMapper tokenMapper,
                                                 SessionHandler sessionHandler) {
        return new GetTokenDataSource(accountsService, tokenMapper, sessionHandler);
    }

    @SessionScope
    @Provides
    GetUserInfoMapper provideGetUserInfoMapper() {
        return new GetUserInfoMapper();
    }

    @SessionScope
    @Provides
    EditUserInfoMapper provideEditUserInfoMapper() {
        return new EditUserInfoMapper();
    }

    @SessionScope
    @Provides
    ProfileSourceFactory provideProfileSourceFactory(@ApplicationContext Context context,
                                                     @Named(BEARER_SERVICE) AccountsService accountsService,
                                                     GetUserInfoMapper getUserInfoMapper,
                                                     EditUserInfoMapper editUserInfoMapper,
                                                     SessionHandler sessionHandler) {
        return new ProfileSourceFactory(
                context, accountsService,
                getUserInfoMapper, editUserInfoMapper,
                sessionHandler);
    }

    @SessionScope
    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @SessionScope
    @Provides
    GetUserInfoUseCase provideGetUserInfoUseCase(ProfileRepository profileRepository) {
        return new GetUserInfoUseCase(profileRepository);
    }

    @SessionScope
    @Provides
    MakeLoginDataSource provideMakeLoginDataSource(@Named(WS_SERVICE) AccountsService accountsService,
                                                   MakeLoginMapper makeLoginMapper,
                                                   SessionHandler sessionHandler) {
        return new MakeLoginDataSource(accountsService, makeLoginMapper, sessionHandler);
    }

    @SessionScope
    @Provides
    CreatePasswordDataSource provideCreatePasswordDataSource(@Named(BEARER_SERVICE) AccountsService
                                                                     accountsService,
                                                             CreatePasswordMapper createPasswordMapper) {
        return new CreatePasswordDataSource(accountsService, createPasswordMapper);
    }

    @SessionScope
    @Provides
    @Named(LOGIN_CACHE)
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, LOGIN_CACHE);
    }

    @SessionScope
    @Provides
    RegisterPhoneNumberMapper provideRegisterPhoneNumberMapper() {
        return new RegisterPhoneNumberMapper();
    }

    @SessionScope
    @Provides
    @RegisterPhoneNumberQualifier
    Retrofit providesRegisterPhoneNumberRetrofit(Retrofit.Builder retrofitBuilder,
                                                 OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(SessionUrl.ACCOUNTS_DOMAIN).client(okHttpClient).build();
    }

    @SessionScope
    @Provides
    RegisterPhoneNumberApi provideRegisterPhoneNumberApi(@RegisterPhoneNumberQualifier Retrofit retrofit) {
        return retrofit.create(RegisterPhoneNumberApi.class);
    }

    @SessionScope
    @Provides
    CloudRegisterPhoneNumberSource provideCloudRegisterPhoneNumberSource(
            @ApplicationContext Context context,
            RegisterPhoneNumberApi registerPhoneNumberApi,
            RegisterPhoneNumberMapper mapper,
            SessionHandler sessionHandler) {
        return new CloudRegisterPhoneNumberSource(context, registerPhoneNumberApi, mapper, sessionHandler);
    }

    @SessionScope
    @Provides
    RegisterPhoneNumberUseCase provideRegisterPhoneNumberUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            @ApplicationContext Context context,
            CloudRegisterPhoneNumberSource source) {
        return new RegisterPhoneNumberUseCase(threadExecutor, postExecutionThread, context, source);
    }

    @SessionScope
    @Provides
    LoginRegisterPhoneNumberUseCase provideLoginRegisterPhoneNumberUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            RegisterPhoneNumberUseCase registerPhoneNumberUseCase,
            GetUserInfoUseCase getUserInfoUseCase,
            MakeLoginUseCase makeLoginUseCase) {
        return new LoginRegisterPhoneNumberUseCase(threadExecutor, postExecutionThread, registerPhoneNumberUseCase, getUserInfoUseCase, makeLoginUseCase);
    }

    @SessionScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @SessionScope
    @Provides
    ChangeNameSource provideChangeNameSource(@Named(BEARER_SERVICE) AccountsService service,
                                             ChangeNameMapper changeNameMapper,
                                             GlobalCacheManager cacheManager) {
        return new ChangeNameSource(service, changeNameMapper, cacheManager);
    }

    @SessionScope
    @Provides
    ChangeNameUseCase provideChangeNameUseCase(ChangeNameSource source) {
        return new ChangeNameUseCase(source);
    }
}


