package com.tokopedia.session.di;

import android.os.Bundle;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.domain.GetTokenUseCase;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.register.data.factory.SessionFactory;
import com.tokopedia.session.register.data.mapper.DiscoverMapper;
import com.tokopedia.session.register.data.repository.SessionRepository;
import com.tokopedia.session.register.data.repository.SessionRepositoryImpl;
import com.tokopedia.session.register.domain.interactor.usecase.DiscoverUseCase;
import com.tokopedia.session.register.domain.interactor.usecase.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.usecase.registerinitial.RegisterFacebookUseCase;

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
                                         @Named(HMAC_SERVICE) AccountsService accountsService,
                                         @Named(BASIC_SERVICE) AccountsService accountsBasicService,
                                         DiscoverMapper discoverMapper,
                                         TokenMapper tokenMapper) {
        return new SessionFactory(globalCacheManager, accountsService, accountsBasicService,
                discoverMapper, tokenMapper);
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
    RegisterFacebookUseCase provideRegisterFacebookUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           GetTokenUseCase getTokenUseCase) {
        return new RegisterFacebookUseCase(threadExecutor, postExecutionThread, getTokenUseCase);
    }


}
