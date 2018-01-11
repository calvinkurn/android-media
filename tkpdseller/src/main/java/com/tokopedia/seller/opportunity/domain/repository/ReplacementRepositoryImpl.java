package com.tokopedia.seller.opportunity.domain.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.source.CloudGetFilterOpportunitySource2;
import com.tokopedia.seller.opportunity.data.source.CloudGetListOpportunitySource2;
import com.tokopedia.seller.opportunity.data.source.CloudActionReplacementSource2;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ReplacementRepositoryImpl implements ReplacementRepository {

    private CloudGetListOpportunitySource2 cloudGetListOpportunitySource2;
    private CloudGetFilterOpportunitySource2 cloudGetFilterOpportunitySource2;
    private CloudActionReplacementSource2 cloudActionReplacementSource2;

    public ReplacementRepositoryImpl(CloudGetListOpportunitySource2 cloudGetListOpportunitySource2,
                                     CloudGetFilterOpportunitySource2 cloudGetFilterOpportunitySource2,
                                     CloudActionReplacementSource2 cloudActionReplacementSource2){
        this.cloudGetListOpportunitySource2 = cloudGetListOpportunitySource2;
        this.cloudGetFilterOpportunitySource2 = cloudGetFilterOpportunitySource2;
        this.cloudActionReplacementSource2 = cloudActionReplacementSource2;
    }

    @Override
    public Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters) {
        RequestParams requestParams = RequestParams.EMPTY;
        requestParams.putAll(parameters);

        return cloudActionReplacementSource2
                .acceptReplacement(requestParams);
    }

    @Override
    public Observable<OpportunityModel> getOpportunityListFromNetwork(TKPDMapParam<String, Object> parameters) {
        RequestParams requestParams = RequestParams.EMPTY;
        requestParams.putAll(parameters);
        return cloudGetListOpportunitySource2.getOpportunityList(requestParams);
    }

    @Override
    public Observable<OpportunityFilterModel> getOpportunityCategoryFromNetwork(TKPDMapParam<String, Object> parameters) {
        RequestParams requestParams = RequestParams.EMPTY;
        requestParams.putAll(parameters);

        return cloudGetFilterOpportunitySource2.getFilter(requestParams);
    }

    @Override
    public Observable<String> getOpportunityReplacementNewPrice(TKPDMapParam<String, Object> parameters) {
        return null;
    }

}
