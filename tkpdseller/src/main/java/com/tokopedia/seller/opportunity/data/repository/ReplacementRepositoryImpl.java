package com.tokopedia.seller.opportunity.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.domain.ReplacementRepository;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ReplacementRepositoryImpl implements ReplacementRepository {

    private final ActionReplacementSourceFactory actionReplacementSourceFactory;
    private final OpportunityDataSourceFactory opportunityDataSourceFactory;

    public ReplacementRepositoryImpl(ActionReplacementSourceFactory actionReplacementSourceFactory,
                                     OpportunityDataSourceFactory opportunityDataSourceFactory) {
        this.actionReplacementSourceFactory = actionReplacementSourceFactory;
        this.opportunityDataSourceFactory = opportunityDataSourceFactory;
    }

    @Override
    public Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters) {
        return actionReplacementSourceFactory.createCloudActionReplacementSource()
                .acceptReplacement(parameters);
    }

    @Override
    public Observable<OpportunityModel> getOpportunityListFromNetwork(TKPDMapParam<String, Object> parameters) {
        return  opportunityDataSourceFactory.createCloudDataListSource()
                .getOpportunityList(parameters);
    }

    @Override
    public Observable<OpportunityCategoryModel> getOpportunityCategoryFromNetwork(TKPDMapParam<String, Object> parameters) {
        return opportunityDataSourceFactory.createCloudFilterReplacementSource()
                .getFilter(parameters);
    }

}
