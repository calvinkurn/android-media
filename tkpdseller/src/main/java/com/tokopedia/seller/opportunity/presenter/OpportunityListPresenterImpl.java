package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.seller.opportunity.domain.param.GetOpportunityListParam;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunityFilterSubscriber;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunitySubscriber;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityListPresenterImpl implements OpportunityListPresenter {

    private final OpportunityListView viewListener;

    private GetOpportunityUseCase getOpportunityUseCase;
    private GetOpportunityFilterUseCase getFilterUseCase;
    private GetOpportunityListParam opportunityParam;
    private SessionHandler sessionHandler;

    public OpportunityListPresenterImpl(OpportunityListView viewListener) {
        this.viewListener = viewListener;
        this.opportunityParam = new GetOpportunityListParam();

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

    }

    @Override
    public void getOpportunity() {
        viewListener.showLoadingList();
        getOpportunityUseCase.execute(getOpportunityParam(), new GetOpportunitySubscriber(viewListener));

    }

    private RequestParams getOpportunityParam() {
        RequestParams param = RequestParams.create();
        param.putString(GetOpportunityUseCase.PAGE, String.valueOf(viewListener.getPage()));
        param.putString(GetOpportunityUseCase.PER_PAGE, GetOpportunityUseCase.DEFAULT_PER_PAGE);
        if (opportunityParam.getQuery() != null)
            param.putString(GetOpportunityUseCase.QUERY, opportunityParam.getQuery());
        if (opportunityParam.getSort() != null && opportunityParam.getKeySort() != null)
            param.putString(opportunityParam.getKeySort(), opportunityParam.getSort());
        if (opportunityParam.getListFilter() != null && opportunityParam.getListFilter().size() > 0) {
            for (FilterPass filterPass : opportunityParam.getListFilter()) {
                param.putString(filterPass.getKey(), filterPass.getValue());
            }
        }
        return param;
    }

    @Override
    public void unsubscribeObservable() {
        getOpportunityUseCase.unsubscribe();
        getFilterUseCase.unsubscribe();
    }

    @Override
    public GetOpportunityListParam getPass() {
        return opportunityParam;
    }

    @Override
    public void getFilter() {
        if (needsToRefresh())
            getFilterUseCase.execute(getOpportunityFilterParam(),
                    new GetOpportunityFilterSubscriber(viewListener));
    }

    private RequestParams getOpportunityFilterParam() {
        RequestParams params = RequestParams.create();
        params.putString(GetOpportunityFilterUseCase.SHOP_ID, sessionHandler.getShopID());
        return params;
    }

    private boolean needsToRefresh() {
        return viewListener.isFilterEmpty();
    }


}
