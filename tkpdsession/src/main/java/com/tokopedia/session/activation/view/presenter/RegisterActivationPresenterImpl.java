package com.tokopedia.session.activation.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
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

    public RegisterActivationPresenterImpl(RegisterActivationView viewListener,
                                           ResendActivationUseCase resendActivationUseCase,
                                           ActivateUnicodeUseCase activateUnicodeUseCase) {
        this.viewListener = viewListener;
        this.resendActivationUseCase = resendActivationUseCase;
        this.activateUnicodeUseCase = activateUnicodeUseCase;
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
