package com.tokopedia.seller.opportunity.domain.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */
public interface ReplacementRepository {

    Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> parameters);

    Observable<OpportunityModel> getOpportunityListFromNetwork(TKPDMapParam<String, Object> parameters);

    Observable<OpportunityFilterModel> getOpportunityCategoryFromNetwork(TKPDMapParam<String, Object> parameters);

    Observable<OpportunityNewPriceData> getOpportunityReplacementNewPrice(RequestParams parameters);

}
