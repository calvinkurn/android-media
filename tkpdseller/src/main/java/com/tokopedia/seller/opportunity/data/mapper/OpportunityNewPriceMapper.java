package com.tokopedia.seller.opportunity.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 1/11/18.
 */

public class OpportunityNewPriceMapper implements Func1<Response<TkpdResponse>, OpportunityNewPriceData> {
    @Override
    public OpportunityNewPriceData call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                return response.body().convertDataObj(OpportunityNewPriceData.class);
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
