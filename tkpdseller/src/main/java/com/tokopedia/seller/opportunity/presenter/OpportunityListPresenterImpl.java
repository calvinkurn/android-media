package com.tokopedia.seller.opportunity.presenter;

import android.support.annotation.Nullable;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFirstTimeUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunityFirstTimeSubscriber;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunitySubscriber;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;
import java.util.HashMap;

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
                               @Nullable ArrayList<FilterPass> listFilter) {
        viewListener.showLoadingList();
        viewListener.disableView();
        getOpportunityUseCase.execute(GetOpportunityUseCase.getRequestParam(
                viewListener.getPage(),
                query,
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
                                            @Nullable ArrayList<FilterPass> listFilter) {
        viewListener.showLoadingList();
        viewListener.disableView();
        getOpportunityFirstTimeUseCase.execute(
                GetOpportunityFirstTimeUseCase.getRequestParam(
                        1,
                        query,
                        listFilter,
                        sessionHandler.getShopID()),
                new GetOpportunityFirstTimeSubscriber(viewListener));

//        if ((listFilter != null && listFilter.size() > 0)
//                || (query != null && !query.equals("")))
//            UnifyTracking.eventOpportunityCustom(
//                    OpportunityTrackingEventLabel.EventName.SUBMIT_OPPORTUNITY,
//                    OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
//                    AppEventTracking.Action.SUBMIT,
//                    OpportunityTrackingEventLabel.EventLabel.SEARCH,
//                    getCustomDimension(query, listFilter)
//            );
    }

    public HashMap<String, String> getCustomDimension(String query,
                                                      ArrayList<FilterPass> listFilter) {
        HashMap<String, String> customDimension = new HashMap<>();
        if (query != null)
            customDimension.put(OpportunityTrackingEventLabel.CustomDimension.SEARCH, query);
        if (listFilter != null)
            for (FilterPass filterPass : listFilter) {
                customDimension.put(filterPass.getKey(), filterPass.getValue() + " - " +
                        filterPass.getName());
            }
        return customDimension;
    }
}
