package com.tokopedia.transaction.opportunity.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.transaction.opportunity.data.mapper.CancelOpportunityMapper;
import com.tokopedia.transaction.opportunity.data.source.CloudCancelReplacementSource;

/**
 * Created by hangnadi on 3/3/17.
 */
public class ActionReplacementSourceFactory {

    private final Context context;
    private ReplacementActService actService;
    private CancelOpportunityMapper cancelMapper;


    public ActionReplacementSourceFactory(Context context) {
        this.context = context;
        this.cancelMapper = new CancelOpportunityMapper();
        this.actService = new ReplacementActService();
    }

    public CloudCancelReplacementSource createCloudCancelReplacementSource() {
        return new CloudCancelReplacementSource(context, actService, cancelMapper);
    }
}
