package com.tokopedia.seller.opportunity.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.mapper.AcceptOpportunityMapper;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementActApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudActionReplacementSource2 {
    private ReplacementActApi replacementActApi;

    @Inject
    public CloudActionReplacementSource2(ReplacementActApi replacementActApi) {
        this.replacementActApi = replacementActApi;
    }

    public Observable<AcceptReplacementModel> acceptReplacement(RequestParams params) {
        return replacementActApi
                .acceptReplacement(params.getParamsAllValueInString())
                .map(new AcceptOpportunityMapper());
    }
}
