package com.tokopedia.session.register.view.di;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.register.data.factory.RegisterEmailSourceFactory;
import com.tokopedia.session.register.data.mapper.RegisterEmailMapper;
import com.tokopedia.session.register.data.model.RegisterViewModel;
import com.tokopedia.session.register.data.repository.RegisterEmailRepositoryImpl;
import com.tokopedia.session.register.domain.interactor.RegisterEmailUseCase;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenter;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenterImpl;
import com.tokopedia.session.register.view.viewlistener.RegisterEmailViewListener;

/**
 * Created by nisie on 5/15/17.
 */

public class RegisterEmailDependencyInjector {
    public static RegisterEmailPresenter getPresenter(RegisterEmailViewListener viewListener) {

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
        String authKey = sessionHandler.getAccessToken(viewListener.getActivity());
        authKey = sessionHandler.getTokenType(viewListener.getActivity()) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);
        RegisterEmailRepositoryImpl registerEmailRepository = new RegisterEmailRepositoryImpl(
                new RegisterEmailSourceFactory(
                        viewListener.getActivity(),
                        accountsService,
                        new RegisterEmailMapper()
                ));

        RegisterEmailUseCase registerEmailUseCase = new RegisterEmailUseCase(
                new JobExecutor(), new UIThread(), registerEmailRepository);

        RegisterViewModel registerViewModel = new RegisterViewModel();

        return new RegisterEmailPresenterImpl(
                viewListener,
                registerEmailUseCase,
                registerViewModel);
    }
}
