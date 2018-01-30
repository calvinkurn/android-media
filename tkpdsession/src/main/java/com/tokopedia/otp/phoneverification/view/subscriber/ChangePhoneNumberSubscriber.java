package com.tokopedia.otp.phoneverification.view.subscriber;

import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.otp.phoneverification.view.listener.ChangePhoneNumber;

import rx.Subscriber;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberSubscriber extends Subscriber<ChangePhoneNumberViewModel> {

    private final ChangePhoneNumber.View viewListener;

    public ChangePhoneNumberSubscriber(ChangePhoneNumber.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorChangePhoneNumber(ErrorHandler.getErrorMessageWithErrorCode(viewListener.getActivity(), e));
    }

    @Override
    public void onNext(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
        if (changePhoneNumberViewModel.isSuccess())
            viewListener.onSuccessChangePhoneNumber();
        else
            viewListener.onErrorChangePhoneNumber(ErrorHandler.getDefaultErrorCodeMessage
                    (ErrorCode.UNSUPPORTED_FLOW));
    }
}
