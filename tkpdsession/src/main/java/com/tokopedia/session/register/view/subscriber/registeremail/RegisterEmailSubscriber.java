package com.tokopedia.session.register.view.subscriber.registeremail;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.register.data.model.RegisterEmailModel;
import com.tokopedia.session.register.data.pojo.RegisterEmailData;
import com.tokopedia.session.register.view.viewlistener.RegisterEmailViewListener;
import com.tokopedia.session.register.view.viewmodel.RegisterEmailViewModel;

import rx.Subscriber;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailSubscriber extends Subscriber<RegisterEmailModel> {

    private static final String ALREADY_REGISTERED = "sudah terdaftar";
    private final RegisterEmailViewListener viewListener;

    public RegisterEmailSubscriber(RegisterEmailViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null
                && e.getLocalizedMessage().contains(ALREADY_REGISTERED)) {
            viewListener.showInfo();
        } else {
            viewListener.onErrorRegister(
                    ErrorHandler.getErrorMessageWithErrorCode(viewListener.getActivity(), e));
        }
    }

    @Override
    public void onNext(RegisterEmailModel registerEmailModel) {
        if (registerEmailModel.isSuccess()) {
            viewListener.onSuccessRegister(mappingToViewModel(registerEmailModel.getRegisterEmailData()));
        } else {
            viewListener.onErrorRegister(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    private RegisterEmailViewModel mappingToViewModel(RegisterEmailData data) {
        RegisterEmailViewModel viewModel = new RegisterEmailViewModel();
        viewModel.setAction(data.getAction());
        viewModel.setIsActive(data.getIsActive());
        viewModel.setIsSuccess(data.getIsSuccess());
        viewModel.setUserId(data.getuId());
        return viewModel;
    }
}
