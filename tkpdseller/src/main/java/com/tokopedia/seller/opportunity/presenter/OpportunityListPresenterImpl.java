package com.tokopedia.seller.opportunity.presenter;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.seller.opportunity.domain.param.GetOpportunityListParam;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunityFilterSubscriber;
import com.tokopedia.seller.opportunity.presenter.subscriber.GetOpportunitySubscriber;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityListPresenterImpl implements OpportunityListPresenter {

    private final OpportunityListView viewListener;

    private GetOpportunityUseCase getOpportunityUseCase;
    private GetOpportunityFilterUseCase getFilterUseCase;
    private GetOpportunityListParam opportunityParam;

    public OpportunityListPresenterImpl(OpportunityListView viewListener) {
        this.viewListener = viewListener;
        this.opportunityParam = new GetOpportunityListParam();

        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
                new ActionReplacementSourceFactory(viewListener.getActivity()),
                new OpportunityDataSourceFactory(viewListener.getActivity())
        );

        this.getOpportunityUseCase = new GetOpportunityUseCase(
                new JobExecutor(), new UIThread(), repository);

        this.getFilterUseCase = new GetOpportunityFilterUseCase(
                new JobExecutor(), new UIThread(), repository);

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
        if (opportunityParam.getSort() != null)
            param.putString(GetOpportunityUseCase.ORDER_BY, opportunityParam.getSort());
        if (opportunityParam.getShippingType() != null)
            param.putString(GetOpportunityUseCase.SHIP_TYPE, opportunityParam.getShippingType());
        if (opportunityParam.getCategory() != null)
            param.putString(GetOpportunityUseCase.CAT_1, opportunityParam.getCategory());
        if (opportunityParam.getQuery() != null)
            param.putString(GetOpportunityUseCase.QUERY, opportunityParam.getQuery());
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
            getFilterUseCase.execute(RequestParams.EMPTY,
                    new GetOpportunityFilterSubscriber(viewListener));
    }

    private boolean needsToRefresh() {
        return viewListener.isFilterEmpty();
    }


}
