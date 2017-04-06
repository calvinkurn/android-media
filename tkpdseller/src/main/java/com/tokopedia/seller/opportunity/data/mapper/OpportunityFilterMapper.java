package com.tokopedia.seller.opportunity.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/6/17.
 */
public class OpportunityFilterMapper implements Func1<Response<TkpdResponse>, OpportunityCategoryModel> {

    @Override
    public OpportunityCategoryModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private OpportunityCategoryModel mappingResponse(Response<TkpdResponse> response) {
        OpportunityCategoryModel model = new OpportunityCategoryModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                OpportunityCategoryData data = response.body().convertDataObj(OpportunityCategoryData.class);
                model.setSuccess(true);
                model.setOpportunityCategoryData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}