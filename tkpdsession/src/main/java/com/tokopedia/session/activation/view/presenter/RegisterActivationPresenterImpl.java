package com.tokopedia.session.activation.view.presenter;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.activation.data.factory.RegisterActivationFactory;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;
import com.tokopedia.session.activation.data.repository.RegisterActivationRepositoryImpl;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;
import com.tokopedia.session.activation.domain.interactor.ResendActivationUseCase;
import com.tokopedia.session.activation.view.subscriber.ResendActivationSubscriber;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;

/**
 * Created by nisie on 1/31/17.
 */

public class RegisterActivationPresenterImpl implements RegisterActivationPresenter {


    private final RegisterActivationView viewListener;
    private ResendActivationUseCase resendActivationUseCase;

    public RegisterActivationPresenterImpl(RegisterActivationView viewListener) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        AccountsService accountsService = new AccountsService(bundle);

        ResendActivationMapper resendActivationMapper = new ResendActivationMapper();
        RegisterActivationFactory registerActivationFactory =
                new RegisterActivationFactory(
                        viewListener.getActivity(),
                        accountsService,
                        resendActivationMapper
                );

        RegisterActivationRepository registerActivationRepository =
                new RegisterActivationRepositoryImpl(registerActivationFactory);

        this.viewListener = viewListener;
        this.resendActivationUseCase = new ResendActivationUseCase(
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

    }

    private RequestParams getResendActivationParam() {
        RequestParams param = RequestParams.create();
        param.putString("email", viewListener.getEmail());
        return param;
    }
}
