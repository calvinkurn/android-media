package com.tokopedia.seller.opportunity.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.data.mapper.AcceptReplacementMapper;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */
public class CloudActionReplacementSource {

    private final Context context;
    private final ReplacementActService actService;
    private final AcceptReplacementMapper acceptMapper;

    public CloudActionReplacementSource(Context context,
                                        ReplacementActService actService,
                                        AcceptReplacementMapper acceptMapper) {
        this.context = context;
        this.actService = actService;
        this.acceptMapper = acceptMapper;
    }

    public Observable<AcceptReplacementModel> acceptReplacement(TKPDMapParam<String, Object> params) {
        return actService.getApi().acceptReplacement(params).map(acceptMapper);
    }
}
