package com.tokopedia.seller.opportunity.data.factory;

import android.content.Context;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.replacement.OpportunityService;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.seller.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.seller.opportunity.data.source.CloudGetFilterOpportunitySource;
import com.tokopedia.seller.opportunity.data.source.CloudGetListOpportunitySource;
import com.tokopedia.seller.opportunity.data.source.local.LocalGetFilterOpportunitySource;

/**
 * Created by hangnadi on 3/3/17.
 */
public class OpportunityDataSourceFactory {
    private final Context context;
    private final OpportunityService opportunityService;
    private final OpportunityListMapper listMapper;
    private final OpportunityFilterMapper filterMapper;
    private final GlobalCacheManager globalCacheManager;

    public OpportunityDataSourceFactory(Context context,
                                        OpportunityService opportunityService,
                                        OpportunityListMapper listMapper,
                                        OpportunityFilterMapper filterMapper,
                                        GlobalCacheManager globalCacheManager) {
        this.context = context;
        this.listMapper = listMapper;
        this.filterMapper = filterMapper;
        this.opportunityService = opportunityService;
        this.globalCacheManager = globalCacheManager;
    }

    public CloudGetListOpportunitySource createCloudDataListSource() {
        return new CloudGetListOpportunitySource(context, opportunityService, listMapper);
    }

    public CloudGetFilterOpportunitySource createCloudFilterReplacementSource() {
        return new CloudGetFilterOpportunitySource(
                context,
                opportunityService,
                filterMapper,
                globalCacheManager);
    }

    public LocalGetFilterOpportunitySource createLocalFilterReplacementSource() {
        return new LocalGetFilterOpportunitySource(globalCacheManager);
    }
}
