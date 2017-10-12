package com.tokopedia.session.activation.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.activation.data.ChangeEmailModel;
import com.tokopedia.session.activation.view.viewListener.ChangeEmailView;

import rx.Subscriber;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailSubscriber extends Subscriber<ChangeEmailModel> {
    private final ChangeEmailView viewListener;

    public ChangeEmailSubscriber(ChangeEmailView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorChangeEmail(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ChangeEmailModel changeEmailModel) {
        if (changeEmailModel.isSuccess()) {
            viewListener.onSuccessChangeEmail();
        } else {
            viewListener.onErrorChangeEmail(changeEmailModel.getErrorMessage());
        }
    }
}
