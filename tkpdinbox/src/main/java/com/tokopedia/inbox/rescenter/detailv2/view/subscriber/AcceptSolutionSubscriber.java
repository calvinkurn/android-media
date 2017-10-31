package com.tokopedia.inbox.rescenter.detailv2.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.DetailResChatFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/22/17.
 */

public class AcceptSolutionSubscriber extends Subscriber<ResolutionActionDomainData> {

    private final DetailResChatFragmentListener.View mainView;

    public AcceptSolutionSubscriber(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.errorAcceptSolution(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(ResolutionActionDomainData data) {
        mainView.successAcceptSolution();
    }
}
