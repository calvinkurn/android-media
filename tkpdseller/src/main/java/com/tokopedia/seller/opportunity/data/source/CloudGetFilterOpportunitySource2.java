package com.tokopedia.seller.opportunity.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudGetFilterOpportunitySource2 {

    private ReplacementApi replacementApi;

    @Inject
    public CloudGetFilterOpportunitySource2(ReplacementApi replacementApi) {
        this.replacementApi = replacementApi;
    }

    public Observable<OpportunityFilterModel> getFilter(RequestParams requestParams){
        return replacementApi.getOpportunityCategory(requestParams.getParamsAllValueInString())
                .map(new OpportunityFilterMapper());
    }
}
