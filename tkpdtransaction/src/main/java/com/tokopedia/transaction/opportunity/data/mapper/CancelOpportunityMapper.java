package com.tokopedia.transaction.opportunity.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 6/2/17.
 */

public class CancelOpportunityMapper implements Func1<Response<TkpdResponse>, CancelReplacementModel> {
    @Override
    public CancelReplacementModel call(Response<TkpdResponse> responseResponse) {
        return null;
    }
}
