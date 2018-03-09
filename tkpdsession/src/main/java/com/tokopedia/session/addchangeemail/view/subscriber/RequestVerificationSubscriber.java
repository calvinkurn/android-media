package com.tokopedia.session.addchangeemail.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailVerificationListener;
import com.tokopedia.session.addchangeemail.view.viewmodel.RequestVerificationViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 09/03/18.
 */

public class RequestVerificationSubscriber extends Subscriber<RequestVerificationViewModel> {

    private AddEmailVerificationListener.View mainView;

    public RequestVerificationSubscriber(AddEmailVerificationListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.dismissLoading();
        mainView.onErrorRequest(ErrorHandler.getErrorMessage(mainView.getContext(), throwable));
    }

    @Override
    public void onNext(RequestVerificationViewModel requestVerificationViewModel) {
        mainView.dismissLoading();
        mainView.onSuccessRequest();
    }
}
