package com.tokopedia.session.activation.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.activation.data.ResendActivationModel;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;

import rx.Subscriber;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationSubscriber extends Subscriber<ResendActivationModel> {

    private final RegisterActivationView viewListener;

    public ResendActivationSubscriber(RegisterActivationView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorResendActivation(ErrorHandler
                .getErrorMessage(e));
    }

    @Override
    public void onNext(ResendActivationModel resendActivationModel) {
        if (resendActivationModel.isSuccess())
            viewListener.onSuccessResendActivation(resendActivationModel.getStatusMessage());
        else {
            viewListener.onErrorResendActivation(resendActivationModel.getErrorMessage());
        }
    }
}
