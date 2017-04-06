package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.mapper.AcceptOpportunityMapper;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */
public class CloudActionReplacementSource {

    private final Context context;
    private final ReplacementActService actService;
    private final AcceptOpportunityMapper acceptMapper;

    public CloudActionReplacementSource(Context context,
                                        ReplacementActService actService,
                                        AcceptOpportunityMapper acceptMapper) {
        this.context = context;
        this.actService = actService;
        this.acceptMapper = acceptMapper;
    }

    public Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> params) {
        return actService.getApi()
                .acceptReplacement(AuthUtil.generateParamsNetwork2(context,params))
                .map(acceptMapper);
    }
}
