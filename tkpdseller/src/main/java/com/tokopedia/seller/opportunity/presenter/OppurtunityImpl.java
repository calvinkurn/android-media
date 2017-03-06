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
import com.tokopedia.seller.opportunity.listener.OppurtunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;

import java.io.IOException;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OppurtunityImpl implements OppurtunityPresenter {

    private final OppurtunityView view;
    private final AcceptReplacementUseCase acceptReplacementUseCase;

    public OppurtunityImpl(Context context, OppurtunityView view) {
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
        acceptReplacementUseCase.execute(getAcceptOppurtunityParams(),
                new AcceptOppurtunitySubscriber(view));
    }

    private RequestParams getAcceptOppurtunityParams() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptReplacementUseCase.PARAMS_ID, "");
        return params;
    }

    private class AcceptOppurtunitySubscriber extends rx.Subscriber<AcceptReplacementModel> {

        private final OppurtunityView view;

        public AcceptOppurtunitySubscriber(OppurtunityView view) {
            this.view = view;
        }

        @Override
        public void onCompleted() {
            view.setOnAcceptOppurtunityComplete();
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
