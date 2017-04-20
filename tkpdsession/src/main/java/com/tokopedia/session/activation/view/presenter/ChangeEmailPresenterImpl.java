package com.tokopedia.session.activation.view.presenter;

import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.data.ChangeEmailPass;
import com.tokopedia.session.activation.data.factory.RegisterActivationFactory;
import com.tokopedia.session.activation.data.mapper.ActivateUnicodeMapper;
import com.tokopedia.session.activation.data.mapper.ChangeEmailMapper;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;
import com.tokopedia.session.activation.data.repository.RegisterActivationRepositoryImpl;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;
import com.tokopedia.session.activation.domain.interactor.ChangeEmailUseCase;
import com.tokopedia.session.activation.view.subscriber.ChangeEmailSubscriber;
import com.tokopedia.session.activation.view.viewListener.ChangeEmailView;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailPresenterImpl implements ChangeEmailPresenter {

    private final ChangeEmailView viewListener;
    private ChangeEmailPass pass;
    private ChangeEmailUseCase changeEmailUseCase;

    public ChangeEmailPresenterImpl(ChangeEmailView viewListener) {
        this.viewListener = viewListener;
        this.pass = new ChangeEmailPass();

        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        AccountsService accountsService = new AccountsService(bundle);

        ResendActivationMapper resendActivationMapper = new ResendActivationMapper();
        ActivateUnicodeMapper activateUnicodeMapper = new ActivateUnicodeMapper();
        ChangeEmailMapper changeEmailMapper = new ChangeEmailMapper();
        RegisterActivationFactory registerActivationFactory =
                new RegisterActivationFactory(
                        viewListener.getActivity(),
                        accountsService, accountsService,
                        resendActivationMapper,
                        activateUnicodeMapper,
                        changeEmailMapper
                );

        RegisterActivationRepository registerActivationRepository =
                new RegisterActivationRepositoryImpl(registerActivationFactory);

        changeEmailUseCase = new ChangeEmailUseCase(
                new JobExecutor(), new UIThread(),
                registerActivationRepository
        );
    }

    @Override
    public ChangeEmailPass getPass() {
        return this.pass;
    }

    @Override
    public void changeEmail() {
        if (isValid()) {
            viewListener.showLoadingProgress();
            changeEmailUseCase.execute(getChangeEmailParam(), new ChangeEmailSubscriber(viewListener));
        }
    }

    private RequestParams getChangeEmailParam() {
        RequestParams params = RequestParams.create();
        params.putString(ChangeEmailUseCase.PARAM_NEW_EMAIL, pass.getNewEmail());
        params.putString(ChangeEmailUseCase.PARAM_OLD_EMAIL, pass.getOldEmail());
        params.putString(ChangeEmailUseCase.PARAM_PASSWORD, pass.getPassword());
        params.putString(ChangeEmailUseCase.PARAM_SEND_EMAIL, ChangeEmailUseCase.DEFAULT_SEND_EMAIL);
        return params;
    }

    @Override
    public void unsubscribeObservable() {
        changeEmailUseCase.unsubscribe();
    }

    private boolean isValid() {
        boolean isValid = true;

        if (viewListener.getPasswordEditText().getText().toString().length() == 0) {
            viewListener.getPasswordEditText().setError(viewListener.getString(R.string.error_field_required));
            viewListener.getPasswordEditText().requestFocus();
            isValid = false;
        }

        if (viewListener.getNewEmailEditText().getText().toString().trim().length() == 0) {
            viewListener.getNewEmailEditText().setError(viewListener.getString(R.string.error_field_required));
            viewListener.getNewEmailEditText().requestFocus();
            isValid = false;
        } else if (!CommonUtils.EmailValidation(viewListener.getNewEmailEditText().getText().toString())) {
            viewListener.getNewEmailEditText().setError(viewListener.getString(R.string.error_invalid_email));
            viewListener.getNewEmailEditText().requestFocus();
            isValid = false;
        }
        return isValid;
    }
}
