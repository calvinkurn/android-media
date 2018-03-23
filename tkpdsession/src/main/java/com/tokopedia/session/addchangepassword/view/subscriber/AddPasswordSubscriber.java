package com.tokopedia.session.addchangepassword.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.session.addchangepassword.view.listener.AddPasswordListener;
import com.tokopedia.session.addchangepassword.view.viewmodel.AddPasswordViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordSubscriber extends Subscriber<AddPasswordViewModel> {
    private AddPasswordListener.View mainView;

    public AddPasswordSubscriber(AddPasswordListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.dismissLoading();
        mainView.onErrorSubmitPassword(ErrorHandler.getErrorMessage(mainView.getContext(), throwable));
    }

    @Override
    public void onNext(AddPasswordViewModel addPasswordViewModel) {
        mainView.dismissLoading();
        if (addPasswordViewModel.isSuccess())
            mainView.onSuccessSubmitPassword();
        else
            mainView.onErrorSubmitPassword(ErrorHandler.getErrorMessage(mainView.getContext(), new Throwable()));
    }
}
