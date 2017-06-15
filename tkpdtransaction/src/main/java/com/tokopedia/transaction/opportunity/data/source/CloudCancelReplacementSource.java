package com.tokopedia.transaction.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.opportunity.data.mapper.CancelOpportunityMapper;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;

import rx.Observable;

/**
 * @author by nisie on 6/2/17.
 *
 */

public class CloudCancelReplacementSource {

    private final Context context;
    private final ReplacementActService actService;
    private final CancelOpportunityMapper cancelOpportunityMapper;

    public CloudCancelReplacementSource(Context context,
                                        ReplacementActService actService,
                                        CancelOpportunityMapper cancelOpportunityMapper) {
        this.context = context;
        this.actService = actService;
        this.cancelOpportunityMapper = cancelOpportunityMapper;
    }

    public Observable<CancelReplacementModel> cancelReplacement(TKPDMapParam<String, Object> params) {
        return actService.getApi()
                .cancelReplacement(AuthUtil.generateParamsNetwork2(context,params))
                .map(cancelOpportunityMapper);
    }
}
