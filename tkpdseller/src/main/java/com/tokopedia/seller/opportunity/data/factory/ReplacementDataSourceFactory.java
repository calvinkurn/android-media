package com.tokopedia.seller.opportunity.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.seller.opportunity.data.mapper.AcceptReplacementMapper;

/**
 * Created by hangnadi on 3/3/17.
 */
public class ReplacementDataSourceFactory {
    private final Context context;
    private ReplacementActService actService;
    private AcceptReplacementMapper acceptMapper;

    public ReplacementDataSourceFactory(Context context) {
        this.context = context;
        this.acceptMapper = new AcceptReplacementMapper();
        this.actService = new ReplacementActService();
    }
}
