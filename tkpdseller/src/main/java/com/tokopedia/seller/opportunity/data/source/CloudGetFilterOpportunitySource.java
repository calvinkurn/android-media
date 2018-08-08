package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.source.api.ReplacementApi;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class CloudGetFilterOpportunitySource {

    private ReplacementApi replacementApi;
    private OpportunityFilterMapper opportunityFilterMapper;
    private Context context;

    @Inject
    public CloudGetFilterOpportunitySource(ReplacementApi replacementApi,
                                           OpportunityFilterMapper opportunityFilterMapper,
                                           @ApplicationContext Context context) {
        this.replacementApi = replacementApi;
        this.opportunityFilterMapper = opportunityFilterMapper;
        this.context = context;
    }

    public Observable<OpportunityFilterModel> getFilter(RequestParams requestParams){
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(context));
        return replacementApi.getOpportunityCategory(requestParams.getParamsAllValueInString())
                .map(opportunityFilterMapper);
    }
}
