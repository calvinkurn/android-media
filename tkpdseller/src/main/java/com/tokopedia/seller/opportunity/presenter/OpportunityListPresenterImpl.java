package com.tokopedia.seller.opportunity.presenter;

import android.support.annotation.Nullable;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFirstTimeUseCase;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.seller.opportunity.domain.param.GetOpportunityListParam;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunityFirstTimeSubscriber;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunitySubscriber;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityListPresenterImpl implements OpportunityListPresenter {

    private final OpportunityListView viewListener;

    private GetOpportunityUseCase getOpportunityUseCase;
    private GetOpportunityFilterUseCase getFilterUseCase;
    private GetOpportunityFirstTimeUseCase getOpportunityFirstTimeUseCase;
    private SessionHandler sessionHandler;

    public OpportunityListPresenterImpl(OpportunityListView viewListener) {
        this.viewListener = viewListener;

        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
                new ActionReplacementSourceFactory(viewListener.getActivity()),
                new OpportunityDataSourceFactory(viewListener.getActivity(),
                        new OpportunityService(),
                        new OpportunityListMapper(),
                        new OpportunityFilterMapper(),
                        new GlobalCacheManager())
        );

        this.getOpportunityUseCase = new GetOpportunityUseCase(
                new JobExecutor(), new UIThread(), repository);

        this.getFilterUseCase = new GetOpportunityFilterUseCase(
                new JobExecutor(), new UIThread(), repository);

        this.sessionHandler = new SessionHandler(viewListener.getActivity());

        this.getOpportunityFirstTimeUseCase = new GetOpportunityFirstTimeUseCase(
                new JobExecutor(), new UIThread(), getOpportunityUseCase, getFilterUseCase
        );

    }

    @Override
    public void getOpportunity(@Nullable String query,
                               @Nullable String keySort,
                               @Nullable String sort,
                               @Nullable ArrayList<FilterPass> listFilter) {
        viewListener.showLoadingList();
        viewListener.disableView();
        getOpportunityUseCase.execute(GetOpportunityUseCase.getRequestParam(
                viewListener.getPage(),
                query,
                keySort,
                sort,
                listFilter
        ), new GetOpportunitySubscriber(viewListener));

    }

    @Override
    public void unsubscribeObservable() {
        getOpportunityUseCase.unsubscribe();
        getFilterUseCase.unsubscribe();
        getOpportunityFirstTimeUseCase.unsubscribe();
    }

    @Override
    public void initOpportunityForFirstTime(@Nullable String query,
                                            @Nullable String keySort,
                                            @Nullable String sort,
                                            @Nullable ArrayList<FilterPass> listFilter) {
        viewListener.showLoadingList();
        viewListener.disableView();
        getOpportunityFirstTimeUseCase.execute(
                GetOpportunityFirstTimeUseCase.getRequestParam(
                        1,
                        query,
                        keySort,
                        sort,
                        listFilter,
                        sessionHandler.getShopID()),
                new GetOpportunityFirstTimeSubscriber(viewListener));
    }

}
