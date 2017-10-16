package com.tokopedia.session.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.data.factory.SessionFactory;
import com.tokopedia.session.data.repository.SessionRepository;
import com.tokopedia.session.data.repository.SessionRepositoryImpl;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.domain.interactor.GetTokenUseCase;
import com.tokopedia.session.domain.mapper.DiscoverMapper;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.RegisterFacebookUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/**
 * @author by nisie on 10/10/17.
 */

@Module
public class SessionModule {

    private static final String HMAC_SERVICE = "HMAC_SERVICE";
    private static final String BASIC_SERVICE = "BASIC_SERVICE";
    private static final String BEARER_SERVICE = "BEARER_SERVICE";

    @SessionScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @SessionScope
    @Named(HMAC_SERVICE)
    @Provides
    AccountsService provideHMACAccountsService() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        return new AccountsService(bundle);
    }

    @SessionScope
    @Named(BASIC_SERVICE)
    @Provides
    AccountsService provideBasicAccountsService() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.IS_BASIC, true);
        return new AccountsService(bundle);
    }

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
    DiscoverMapper provideDiscoverMapper() {
        return new DiscoverMapper();
    }

    @SessionScope
    @Provides
    SessionFactory provideSessionFactory(GlobalCacheManager globalCacheManager,
                                         SessionHandler sessionHandler,
                                         @Named(HMAC_SERVICE) AccountsService accountsService,
                                         @Named(BASIC_SERVICE) AccountsService accountsBasicService,
                                         @Named(BEARER_SERVICE) AccountsService
                                                 accountsBearerService,
                                         DiscoverMapper discoverMapper,
                                         TokenMapper tokenMapper) {
        return new SessionFactory(globalCacheManager, sessionHandler, accountsService,
                accountsBasicService, accountsBearerService, discoverMapper, tokenMapper);
    }

    @SessionScope
    @Provides
    SessionRepository provideSessionRepository(SessionFactory sessionFactory) {
        return new SessionRepositoryImpl(sessionFactory);
    }

    @SessionScope
    @Provides
    DiscoverUseCase provideDiscoverUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           SessionRepository sessionRepository) {
        return new DiscoverUseCase(threadExecutor, postExecutionThread, sessionRepository);
    }

    @SessionScope
    @Provides
    GetFacebookCredentialUseCase provideGetFacebookCredentialUseCase() {
        return new GetFacebookCredentialUseCase();
    }

    @SessionScope
    @Provides
    GetTokenUseCase provideGetTokenUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           SessionRepository sessionRepository) {
        return new GetTokenUseCase(threadExecutor, postExecutionThread, sessionRepository);
    }

    @SessionScope
    @Provides
    TokenMapper provideTokenMapper() {
        return new TokenMapper();
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
                                                     EditUserInfoMapper editUserInfoMapper) {
        return new ProfileSourceFactory(
                context, accountsService,
                getUserInfoMapper, editUserInfoMapper);
    }

    @SessionScope
    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }


    @SessionScope
    @Provides
    GetUserInfoUseCase provideGetUserInfoUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 ProfileRepository profileRepository) {
        return new GetUserInfoUseCase(threadExecutor, postExecutionThread, profileRepository);
    }


    @SessionScope
    @Provides
    RegisterFacebookUseCase provideRegisterFacebookUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           GetTokenUseCase getTokenUseCase,
                                                           GetUserInfoUseCase getUserInfoUseCase) {
        return new RegisterFacebookUseCase(
                threadExecutor, postExecutionThread,
                getTokenUseCase, getUserInfoUseCase);
    }

}
