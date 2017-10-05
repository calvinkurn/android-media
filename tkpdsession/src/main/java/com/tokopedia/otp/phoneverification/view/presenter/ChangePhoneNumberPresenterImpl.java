package com.tokopedia.otp.phoneverification.view.presenter;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.data.factory.MsisdnSourceFactory;
import com.tokopedia.otp.phoneverification.data.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.repository.MsisdnRepositoryImpl;
import com.tokopedia.otp.phoneverification.domain.interactor.ChangePhoneNumberUseCase;
import com.tokopedia.otp.phoneverification.view.listener.ChangePhoneNumberView;
import com.tokopedia.otp.phoneverification.view.subscriber.ChangePhoneNumberSubscriber;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/24/17.
 */

public class ChangePhoneNumberPresenterImpl implements ChangePhoneNumberPresenter {

    private static final String TOKEN_BEARER = "Bearer ";
    private final ChangePhoneNumberView viewListener;
    private ChangePhoneNumberUseCase changePhoneNumberUseCase;

    public ChangePhoneNumberPresenterImpl(ChangePhoneNumberView viewListener) {
        this.viewListener = viewListener;

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
        bundle.putString(AccountsService.AUTH_KEY,
                TOKEN_BEARER + sessionHandler.getAccessToken(viewListener.getActivity()));
        bundle.putBoolean(AccountsService.USING_BOTH_AUTHORIZATION,
                true);
        AccountsService accountsService = new AccountsService(bundle);

        MsisdnSourceFactory msisdnSourceFactory = new MsisdnSourceFactory(
                viewListener.getActivity(),
                accountsService,
                new VerifyPhoneNumberMapper(),
                new ChangePhoneNumberMapper()
        );

        this.changePhoneNumberUseCase = new ChangePhoneNumberUseCase(
                new JobExecutor(),
                new UIThread(),
                new MsisdnRepositoryImpl(msisdnSourceFactory));
    }

    @Override
    public void changePhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            changePhoneNumberUseCase.execute(getChangePhoneNumberParam(),
                    new ChangePhoneNumberSubscriber(viewListener));
        }
    }

    private RequestParams getChangePhoneNumberParam() {
        RequestParams params = RequestParams.create();
        params.putString(ChangePhoneNumberUseCase.PARAM_MSISDN,
                viewListener.getPhoneNumberEditText().getText().toString().replace("-", ""));
        return params;
    }

    @Override
    public void unsubscribeObservable() {
        changePhoneNumberUseCase.unsubscribe();
    }

    private boolean isValid(String phoneNumber) {
        boolean isValid = true;
        if (phoneNumber.length() == 0) {
            isValid = false;
            viewListener.getPhoneNumberEditText().setError(
                    viewListener.getString(R.string.error_field_required));
            viewListener.getPhoneNumberEditText().requestFocus();
        }
        return isValid;
    }
}
