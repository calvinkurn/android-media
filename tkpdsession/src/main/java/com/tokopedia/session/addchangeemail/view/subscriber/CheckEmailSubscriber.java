package com.tokopedia.session.addchangeemail.view.subscriber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailListener;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailSubscriber extends Subscriber<CheckEmailViewModel> {

    private AddEmailListener.View mainView;

    public CheckEmailSubscriber(AddEmailListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.dismissLoading();
        mainView.onErrorCheckEmail(ErrorHandler.getErrorMessage(throwable));
    }

    @Override
    public void onNext(CheckEmailViewModel checkEmailViewModel) {
        mainView.dismissLoading();
        if (!checkEmailViewModel.isExist()) {
            mainView.onSuccessCheckEmail();
        } else {
            mainView.onErrorCheckEmail(mainView.getContext().getResources().getString(R.string.email_already_registered));
        }
    }
}
