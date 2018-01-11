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

    private final OpportunityView view;
    private AcceptReplacementUseCase acceptReplacementUseCase;



    @Deprecated
    public OpportunityImpl(Context context, OpportunityView view) {
        this.view = view;

//        OpportunityService opportunityService = new OpportunityService();
//
//        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
//                new ActionReplacementSourceFactory(context),
//                new OpportunityDataSourceFactory(context,
//                        opportunityService,
//                        new OpportunityListMapper(),
//                        new OpportunityFilterMapper(),
//                        new GlobalCacheManager())
//        );
//        this.acceptReplacementUseCase = new AcceptReplacementUseCase(
//                new JobExecutor(), new UIThread(), repository
//        );
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
