package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditAppealResolutionSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class AppealSolutionWithRefundSubscriber extends Subscriber<EditAppealResolutionSolutionDomain> {
    private final SolutionDetailFragmentListener.View mainView;

    public AppealSolutionWithRefundSubscriber(SolutionDetailFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.errorEditSolution(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(EditAppealResolutionSolutionDomain editAppealResolutionSolutionDomain) {
        if (editAppealResolutionSolutionDomain.isSuccess()) {
            mainView.successEditSolution(editAppealResolutionSolutionDomain.getMessage());
        } else {
            mainView.errorEditSolution(editAppealResolutionSolutionDomain.getMessage());
        }
    }

}
