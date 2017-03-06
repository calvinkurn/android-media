package com.tokopedia.seller.opportunity.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.data.source.CloudGetFilterOpportunitySource;
import com.tokopedia.seller.opportunity.data.source.CloudGetListOpportunitySource;

/**
 * Created by hangnadi on 3/3/17.
 */
public class OpportunityDataSourceFactory {
    private final Context context;
    private OpportunityService opportunityService;
    private OpportunityListMapper listMapper;
    private OpportunityFilterMapper filterMapper;

    public OpportunityDataSourceFactory(Context context) {
        this.context = context;
        this.listMapper = new OpportunityListMapper();
        this.opportunityService = new OpportunityService();
    }

    public CloudGetListOpportunitySource createCloudDataListSource() {
        return new CloudGetListOpportunitySource(context, opportunityService, listMapper);
    }

    public CloudGetFilterOpportunitySource createCloudFilterReplacementSource() {
        return new CloudGetFilterOpportunitySource(context, opportunityService, filterMapper);
    }
}
