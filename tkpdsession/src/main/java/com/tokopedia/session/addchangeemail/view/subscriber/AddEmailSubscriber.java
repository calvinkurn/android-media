package com.tokopedia.session.addchangeemail.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailVerificationListener;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailSubscriber extends Subscriber<AddEmailViewModel> {

    private AddEmailVerificationListener.View mainView;

    public AddEmailSubscriber(AddEmailVerificationListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.dismissLoading();
        mainView.onErrorVerify(ErrorHandler.getErrorMessage(mainView.getContext(), throwable));
    }

    @Override
    public void onNext(AddEmailViewModel addEmailViewModel) {
        mainView.dismissLoading();
        if (addEmailViewModel.isSuccess())
            mainView.onSuccessVerify();
        else
            mainView.onErrorVerify(ErrorHandler.getErrorMessage(mainView.getContext(), new Throwable()));
    }
}
