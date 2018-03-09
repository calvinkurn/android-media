package com.tokopedia.session.addchangeemail.view.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailListener;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailSubscriber extends Subscriber<CheckEmailViewModel> {

    private AddEmailListener.View mainView;
    private Context context;

    public CheckEmailSubscriber(Context context, AddEmailListener.View mainView) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.onErrorCheckEmail(ErrorHandler.getErrorMessage(context, throwable));
    }

    @Override
    public void onNext(CheckEmailViewModel checkEmailViewModel) {
        mainView.onSuccessCheckEmail();
    }
}
