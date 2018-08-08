package com.tokopedia.session.register.view.subscriber;

import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.register.domain.model.CreatePasswordLoginDomain;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;

import rx.Subscriber;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordSubscriber extends Subscriber<CreatePasswordLoginDomain> {
    private final CreatePassword.View viewListener;

    public CreatePasswordSubscriber(CreatePassword.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorCreatePassword(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(CreatePasswordLoginDomain domain) {
        if (domain.getCreatePasswordDomain().isSuccess()
                && domain.getMakeLoginDomain().isLogin()
                && !domain.getMakeLoginDomain().isMsisdnVerified()) {
            viewListener.onGoToPhoneVerification();
        } else if (domain.getCreatePasswordDomain().isSuccess()
                && domain.getMakeLoginDomain().isLogin()
                && !domain.getMakeLoginDomain().isMsisdnVerified()) {
            viewListener.onSuccessCreatePassword();
        } else {
            viewListener.onErrorCreatePassword(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode
                    .UNSUPPORTED_FLOW));
        }
    }
}
