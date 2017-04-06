package com.tokopedia.seller.opportunity.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presentation.ActionViewData;
import com.tokopedia.seller.opportunity.presenter.subscriber.AcceptOpportunitySubscriber;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
    public void acceptOpportunity() {
        view.showLoadingProgress();
        acceptReplacementUseCase.execute(getAcceptOpportunityParams(),
                new AcceptOpportunitySubscriber(view));
    }

    private RequestParams getAcceptOpportunityParams() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptReplacementUseCase.PARAMS_ID, view.getOpportunityId());
        return params;
    }


    @Override
    public void unsubscribeObservable() {
        acceptReplacementUseCase.unsubscribe();
    }

}
