package com.tokopedia.seller.opportunity.snapshot.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;

import rx.Subscriber;

/**
 * @author by nisie on 5/31/17.
 */

public class AcceptOpportunitySubscriberFromSnapshot extends Subscriber<AcceptReplacementModel> {


    private final SnapShotFragmentView viewListener;

    public AcceptOpportunitySubscriberFromSnapshot(SnapShotFragmentView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorTakeOpportunity(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(AcceptReplacementModel acceptReplacementModel) {
        if (acceptReplacementModel.isSuccess())
            viewListener.onSuccessTakeOpportunity(mappingAcceptReplacementViewModel(acceptReplacementModel));
        else
            viewListener.onErrorTakeOpportunity(mappingAcceptReplacementViewModel(acceptReplacementModel).getMessage());
    }

    private ActionViewData mappingAcceptReplacementViewModel(AcceptReplacementModel model) {
        ActionViewData data = new ActionViewData();
        data.setSuccess(model.isSuccess());
        data.setMessage(model.getMessage());
        return data;
    }
}
