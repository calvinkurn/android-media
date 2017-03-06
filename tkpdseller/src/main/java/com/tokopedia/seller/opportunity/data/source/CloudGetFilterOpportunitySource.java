package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;

import rx.Observable;

/**
 * Created by nisie on 3/6/17.
 */
public class CloudGetFilterOpportunitySource {

    private final Context context;
    private final OpportunityService opportunityService;
    private final OpportunityFilterMapper mapper;

    public CloudGetFilterOpportunitySource(Context context,
                                           OpportunityService opportunityService,
                                           OpportunityFilterMapper mapper) {
        this.context = context;
        this.opportunityService = opportunityService;
        this.mapper = mapper;
    }

    public Observable<OpportunityCategoryModel> getFilter(TKPDMapParam<String, Object> params) {
        return opportunityService.getApi().getOpportunityCategory(params).map(mapper);
    }
}
