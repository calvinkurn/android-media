package com.tokopedia.session.register.view.di;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.register.data.factory.RegisterEmailSourceFactory;
import com.tokopedia.session.register.data.mapper.RegisterEmailMapper;
import com.tokopedia.session.register.data.model.RegisterViewModel;
import com.tokopedia.session.register.data.repository.RegisterEmailRepositoryImpl;
import com.tokopedia.session.register.domain.interactor.registeremail.RegisterEmailUseCase;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenter;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenterImpl;
import com.tokopedia.session.register.view.viewlistener.RegisterEmailViewListener;

/**
 * Created by nisie on 5/15/17.
 */

public class RegisterEmailDependencyInjector {
    public static RegisterEmailPresenter getPresenter(RegisterEmailViewListener viewListener) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

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
