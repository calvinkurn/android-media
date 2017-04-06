package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;

import rx.Observable;

/**
 * Created by nisie on 3/3/17.
 */
public class CloudGetListOpportunitySource {

    private final Context context;
    private final OpportunityService opportunityService;
    private final OpportunityListMapper mapper;

    public CloudGetListOpportunitySource(Context context,
                                         OpportunityService opportunityService,
                                         OpportunityListMapper mapper) {
        this.context = context;
        this.opportunityService = opportunityService;
        this.mapper = mapper;
    }

    public Observable<OpportunityModel> getOpportunityList(TKPDMapParam<String, Object> params) {
        return opportunityService.getApi()
                .getOpportunityList(AuthUtil.generateParamsNetwork2(context, params))
                .map(mapper);
    }
}
