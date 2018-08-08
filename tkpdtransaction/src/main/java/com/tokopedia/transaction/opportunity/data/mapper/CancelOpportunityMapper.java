package com.tokopedia.transaction.opportunity.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.replacement.AcceptReplacementData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;
import com.tokopedia.transaction.opportunity.data.pojo.CancelReplacementPojo;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 6/2/17.
 */

public class CancelOpportunityMapper implements Func1<Response<TkpdResponse>, CancelReplacementModel> {
    @Override
    public CancelReplacementModel call(Response<TkpdResponse> response) {
        CancelReplacementModel model = new CancelReplacementModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                CancelReplacementPojo data = response.body().convertDataObj(CancelReplacementPojo.class);
                model.setSuccess(data != null && data.getIsSuccess() == 1);
                model.setStatusMessage(data != null ? data.getMessageStatus() : "");
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
