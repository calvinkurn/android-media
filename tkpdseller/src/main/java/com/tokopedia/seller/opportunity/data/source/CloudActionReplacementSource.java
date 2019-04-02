package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.mapper.AcceptOpportunityMapper;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementActApi;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudActionReplacementSource {
    private ReplacementActApi replacementActApi;
    private AcceptOpportunityMapper acceptOpportunityMapper;
    private Context context;

    @Inject
    public CloudActionReplacementSource(ReplacementActApi replacementActApi, AcceptOpportunityMapper acceptOpportunityMapper,
                                        @ApplicationContext Context context) {
        this.replacementActApi = replacementActApi;
        this.acceptOpportunityMapper = acceptOpportunityMapper;
        this.context = context;
    }

    public Observable<AcceptReplacementModel> acceptReplacement(RequestParams params) {
        params.putAll((HashMap<String, String>) AuthUtil.generateParams(context));
        return replacementActApi
                .acceptReplacement(params.getParamsAllValueInString())
                .map(acceptOpportunityMapper);
    }
}
