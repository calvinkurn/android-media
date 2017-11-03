package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;

import rx.Subscriber;

/**
 * Created by yoasfs on 11/3/17.
 */

public class InputAddressAcceptSolutionSubscriber extends Subscriber<ResolutionActionDomainData> {

    private final DetailResChatFragmentListener.View mainView;

    public InputAddressAcceptSolutionSubscriber(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.errorInputAddress(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ResolutionActionDomainData data) {
        if (data.isSuccess()) {
            mainView.successInputAddress();
        } else {
            mainView.errorInputAddress(data.getMessageError());
        }
    }
}
