package com.tokopedia.session.changename.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.session.changename.view.listener.ChangeNameListener;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameSubscriber extends Subscriber<ChangeNameViewModel> {
    private ChangeNameListener.View mainView;

    public ChangeNameSubscriber(ChangeNameListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.dismissLoading();
        mainView.onErrorSubmitName(ErrorHandler.getErrorMessage(mainView.getContext(), throwable));
    }

    @Override
    public void onNext(ChangeNameViewModel changeNameViewModel) {
        mainView.dismissLoading();
        if (changeNameViewModel.isSuccess())
            mainView.onSuccessSubmitName();
        else
            mainView.onErrorSubmitName(ErrorHandler.getErrorMessage(mainView.getContext(), new Throwable()));
    }
}
