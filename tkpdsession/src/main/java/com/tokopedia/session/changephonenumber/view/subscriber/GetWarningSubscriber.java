package com.tokopedia.session.changephonenumber.view.subscriber;

import android.util.Log;

import com.tkpd.library.utils.Logger;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningSubscriber extends rx.Subscriber<WarningViewModel> {
    private final ChangePhoneNumberWarningFragmentListener.View view;

    public GetWarningSubscriber(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onGetWarningFailed();
    }

    @Override
    public void onNext(WarningViewModel object) {
        view.onGetWarningSuccess(object);
    }
}
