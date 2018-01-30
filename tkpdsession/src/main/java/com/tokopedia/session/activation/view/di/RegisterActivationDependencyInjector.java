package com.tokopedia.session.activation.view.di;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.activation.data.factory.RegisterActivationFactory;
import com.tokopedia.session.activation.data.mapper.ActivateUnicodeMapper;
import com.tokopedia.session.activation.data.mapper.ChangeEmailMapper;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;
import com.tokopedia.session.activation.data.repository.RegisterActivationRepositoryImpl;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;
import com.tokopedia.session.activation.domain.interactor.ActivateUnicodeUseCase;
import com.tokopedia.session.activation.domain.interactor.ResendActivationUseCase;
import com.tokopedia.session.activation.view.presenter.RegisterActivationPresenter;
import com.tokopedia.session.activation.view.presenter.RegisterActivationPresenterImpl;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;

/**
 * Created by nisie on 5/15/17.
 */

public class RegisterActivationDependencyInjector {
    public static RegisterActivationPresenter getPresenter(RegisterActivationView viewListener) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        AccountsService accountsService = new AccountsService(bundle);

        AccountsService accountsServiceBearer = new AccountsService(new Bundle());

        ResendActivationMapper resendActivationMapper = new ResendActivationMapper();
        ActivateUnicodeMapper activateUnicodeMapper = new ActivateUnicodeMapper();
        ChangeEmailMapper changeEmailMapper = new ChangeEmailMapper();
        RegisterActivationFactory registerActivationFactory =
                new RegisterActivationFactory(
                        viewListener.getActivity(),
                        accountsService,
                        accountsServiceBearer,
                        resendActivationMapper,
                        activateUnicodeMapper,
                        changeEmailMapper
                );

        RegisterActivationRepository registerActivationRepository =
                new RegisterActivationRepositoryImpl(registerActivationFactory);

        ResendActivationUseCase resendActivationUseCase = new ResendActivationUseCase(
                new JobExecutor(), new UIThread(), registerActivationRepository
        );
        ActivateUnicodeUseCase activateUnicodeUseCase = new ActivateUnicodeUseCase(
                new JobExecutor(), new UIThread(), registerActivationRepository
        );

        return new RegisterActivationPresenterImpl(
                viewListener,
                resendActivationUseCase,
                activateUnicodeUseCase);
    }
}
