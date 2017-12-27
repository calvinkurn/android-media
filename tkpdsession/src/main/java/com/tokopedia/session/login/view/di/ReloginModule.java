package com.tokopedia.session.login.view.di;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.login.MakeLoginUseCase;
import com.tokopedia.session.login.domain.factory.LoginFactory;
import com.tokopedia.session.login.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.login.domain.repository.LoginRepository;
import com.tokopedia.session.login.domain.repository.LoginRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 5/26/17.
 */

@Module
class ReloginModule {

    @ReloginScope
    @Provides
    Bundle provideAccountsBundle(@ApplicationContext Context context,
                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + sessionHandler.getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        return bundle;
    }


    @ReloginScope
    @Provides
    AccountsService provideAccountsService(SessionHandler sessionHandler,
                                           Bundle bundle) {
        return new AccountsService(bundle);
    }

    @ReloginScope
    @Provides
    LoginFactory provideLoginFactory(@ApplicationContext Context context,
                                     AccountsService accountsService,
                                     MakeLoginMapper makeLoginMapper) {

        return new LoginFactory(
                context, accountsService, makeLoginMapper);
    }


    @ReloginScope
    @Provides
    MakeLoginMapper provideMakeLoginMapper() {
        return new MakeLoginMapper();
    }

    @ReloginScope
    @Provides
    LoginRepository provideLoginRepository(LoginFactory loginFactory) {

        return new LoginRepositoryImpl(loginFactory);
    }

    @ReloginScope
    @Provides
    MakeLoginUseCase provideMakeLoginUseCase(ThreadExecutor threadExecutor,
                                             PostExecutionThread uiThread,
                                             LoginRepository loginRepository) {

        return new MakeLoginUseCase(threadExecutor, uiThread, loginRepository);
    }

}
