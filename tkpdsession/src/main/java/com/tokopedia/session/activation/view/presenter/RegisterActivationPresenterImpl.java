package com.tokopedia.session.activation.view.presenter;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.activation.data.factory.RegisterActivationFactory;
import com.tokopedia.session.activation.data.mapper.ActivateUnicodeMapper;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;
import com.tokopedia.session.activation.data.repository.RegisterActivationRepositoryImpl;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;
import com.tokopedia.session.activation.domain.interactor.ActivateUnicodeUseCase;
import com.tokopedia.session.activation.domain.interactor.ResendActivationUseCase;
import com.tokopedia.session.activation.view.subscriber.ActivateUnicodeSubscriber;
import com.tokopedia.session.activation.view.subscriber.ResendActivationSubscriber;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;

/**
 * Created by nisie on 1/31/17.
 */

public class RegisterActivationPresenterImpl implements RegisterActivationPresenter {


    private final RegisterActivationView viewListener;
    private ResendActivationUseCase resendActivationUseCase;
    private ActivateUnicodeUseCase activateUnicodeUseCase;

    public RegisterActivationPresenterImpl(RegisterActivationView viewListener) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        AccountsService accountsService = new AccountsService(bundle);

        ResendActivationMapper resendActivationMapper = new ResendActivationMapper();
        ActivateUnicodeMapper activateUnicodeMapper = new ActivateUnicodeMapper();
        RegisterActivationFactory registerActivationFactory =
                new RegisterActivationFactory(
                        viewListener.getActivity(),
                        accountsService,
                        resendActivationMapper,
                        activateUnicodeMapper
                );

        RegisterActivationRepository registerActivationRepository =
                new RegisterActivationRepositoryImpl(registerActivationFactory);

        this.viewListener = viewListener;
        this.resendActivationUseCase = new ResendActivationUseCase(
                new JobExecutor(), new UIThread(), registerActivationRepository
        );
        this.activateUnicodeUseCase = new ActivateUnicodeUseCase(
                new JobExecutor(), new UIThread(), registerActivationRepository
        );
    }

    @Override
    public void resendActivation() {
        viewListener.showLoadingProgress();
        resendActivationUseCase.execute(getResendActivationParam(),
                new ResendActivationSubscriber(viewListener));

    }

    @Override
    public void activateAccount() {
        viewListener.showLoadingProgress();
        activateUnicodeUseCase.execute(getActivateUnicodeParam(),
                new ActivateUnicodeSubscriber(viewListener));
    }

    @Override
    public void unsubscribeObservable() {
        resendActivationUseCase.unsubscribe();
        activateUnicodeUseCase.unsubscribe();
    }

    private RequestParams getActivateUnicodeParam() {
        RequestParams param = RequestParams.create();
        param.putString(ActivateUnicodeUseCase.PARAM_EMAIL, viewListener.getEmail());
        param.putString(ActivateUnicodeUseCase.PARAM_UNICODE, viewListener.getUnicode());
        param.putString(ActivateUnicodeUseCase.PARAM_GRANT_TYPE,
                ActivateUnicodeUseCase.DEFAULT_GRANT_TYPE);
        param.putString(ActivateUnicodeUseCase.PARAM_PASSWORD_TYPE,
                ActivateUnicodeUseCase.DEFAULT_PASSWORD_TYPE);
        return param;
    }

    private RequestParams getResendActivationParam() {
        RequestParams param = RequestParams.create();
        param.putString(ResendActivationUseCase.PARAM_EMAIL, viewListener.getEmail());
        return param;
    }
}
