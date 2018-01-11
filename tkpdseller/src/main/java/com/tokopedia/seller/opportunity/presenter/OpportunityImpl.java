package com.tokopedia.seller.opportunity.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.seller.opportunity.listener.OpportunityView;
import com.tokopedia.seller.opportunity.presenter.subscriber.AcceptOpportunitySubscriber;

/**
 * Created by hangnadi on 2/27/17.
 */
public class OpportunityImpl extends OpportunityPresenter {

    private AcceptReplacementUseCase acceptReplacementUseCase;


    public OpportunityImpl(AcceptReplacementUseCase acceptReplacementUseCase) {
        this.acceptReplacementUseCase = acceptReplacementUseCase;
    }

    @Override
    public void acceptOpportunity() {
        getView().showLoadingProgress();
        acceptReplacementUseCase.execute(getAcceptOpportunityParams(),
                new AcceptOpportunitySubscriber(getView()));
    }

    private RequestParams getAcceptOpportunityParams() {
        RequestParams params = RequestParams.create();
        params.putString(AcceptReplacementUseCase.PARAMS_ID, getView().getOpportunityId());
        return params;
    }


    @Override
    public void unsubscribeObservable() {
        acceptReplacementUseCase.unsubscribe();
    }


}
