package com.tokopedia.seller.opportunity.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;

import java.io.IOException;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OpportunityImpl implements OpportunityPresenter {

    private final OpportunityView view;
    private final AcceptReplacementUseCase acceptReplacementUseCase;

    public OpportunityImpl(Context context, OpportunityView view) {
        this.view = view;
        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
                new ActionReplacementSourceFactory(context),
                new OpportunityDataSourceFactory(context)
        );
        this.acceptReplacementUseCase = new AcceptReplacementUseCase(
                new JobExecutor(), new UIThread(), repository
        );
    }

    @Override
    public void setOnSubmitClickListener() {
        acceptReplacementUseCase.execute(getAcceptOpportunityParams(),
                new AcceptOpportunitySubscriber(view));
    }

    private RequestParams getAcceptOpportunityParams() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptReplacementUseCase.PARAMS_ID, view.getOpportunityId());
        return params;
    }

    private class AcceptOpportunitySubscriber extends rx.Subscriber<AcceptReplacementModel> {

        private final OpportunityView view;

        public AcceptOpportunitySubscriber(OpportunityView view) {
            this.view = view;
        }

        @Override
        public void onCompleted() {
            view.setOnAcceptOpportunityComplete();
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof IOException) {
                view.setActionViewData(mappingTimeout());
            }
        }

        @Override
        public void onNext(AcceptReplacementModel acceptReplacementModel) {
            view.setActionViewData(mappingAcceptReplacementViewModel(acceptReplacementModel));
        }
    }

    private ActionViewData mappingTimeout() {
        ActionViewData data = new ActionViewData();
        data.setSuccess(false);
        data.setTimeOut(true);
        return data;
    }

    private ActionViewData mappingAcceptReplacementViewModel(AcceptReplacementModel model) {
        ActionViewData data = new ActionViewData();
        data.setSuccess(model.isSuccess());
        data.setErrorCode(model.getErrorCode());
        data.setMessageError(model.getErrorMessage());
        return data;
    }
}
