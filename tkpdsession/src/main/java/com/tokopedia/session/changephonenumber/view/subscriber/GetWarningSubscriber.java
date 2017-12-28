package com.tokopedia.session.changephonenumber.view.subscriber;

import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import rx.Subscriber;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningSubscriber extends Subscriber<WarningViewModel> {
    private final ChangePhoneNumberWarningFragmentListener.View view;

    public GetWarningSubscriber(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoading();
        if (e != null)
            view.onGetWarningError(e.getMessage());
        else
            view.onGetWarningFailed();
    }

    @Override
    public void onNext(WarningViewModel object) {
        view.dismissLoading();
        view.onGetWarningSuccess(object);
    }
}
