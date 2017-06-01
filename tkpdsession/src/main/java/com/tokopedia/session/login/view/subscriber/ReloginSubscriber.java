package com.tokopedia.session.login.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;
import com.tokopedia.session.login.view.ReloginContract;

import rx.Subscriber;

/**
 * @author by nisie on 5/26/17.
 */

public class ReloginSubscriber extends Subscriber<MakeLoginDomainModel> {

    private final ReloginContract.View viewListener;

    public ReloginSubscriber(ReloginContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRelogin(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(MakeLoginDomainModel makeLoginDomainModel) {
        if (makeLoginDomainModel.isSuccess())
            viewListener.onSuccessRelogin();
        else
            viewListener.onErrorRelogin(makeLoginDomainModel.getErrorMessage());
    }
}
