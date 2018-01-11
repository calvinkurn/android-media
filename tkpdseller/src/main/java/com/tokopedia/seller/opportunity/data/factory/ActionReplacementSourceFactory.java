package com.tokopedia.seller.opportunity.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.seller.opportunity.data.mapper.AcceptOpportunityMapper;

/**
 * Created by hangnadi on 3/3/17.
 */
@Deprecated
public class ActionReplacementSourceFactory {

    private final Context context;
    private ReplacementActService actService;
    private AcceptOpportunityMapper acceptMapper;


    public ActionReplacementSourceFactory(Context context) {
        this.context = context;
        this.acceptMapper = new AcceptOpportunityMapper();
        this.actService = new ReplacementActService();
    }

}
