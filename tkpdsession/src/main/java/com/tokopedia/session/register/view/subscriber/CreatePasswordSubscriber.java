package com.tokopedia.session.register.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 10/16/17.
 */

public class CreatePasswordSubscriber extends Subscriber<CreatePasswordViewModel> {
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
    public void onNext(CreatePasswordViewModel createPasswordViewModel) {
        if(createPasswordViewModel.isSuccess()){
            viewListener.onSuccessCreatePassword();
        }else{
            viewListener.onErrorCreatePassword(MainApplication.getAppContext().getString(R
                    .string.error_empty_provider) + " " + MainApplication.getAppContext().getString(R
                    .string.code_error) + " " + ErrorCode.WS_ERROR);

        }
    }
}
