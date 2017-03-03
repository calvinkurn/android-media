package com.tokopedia.seller.opportunity.data.mapper;

import com.tokopedia.core.network.entity.replacement.AcceptReplacementData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/3/17.
 */
public class AcceptReplacementMapper
        implements Func1<Response<TkpdResponse>, AcceptReplacementModel>{

    @Override
    public AcceptReplacementModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private AcceptReplacementModel mappingResponse(Response<TkpdResponse> response) {
        AcceptReplacementModel model = new AcceptReplacementModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                AcceptReplacementData data = response.body().convertDataObj(AcceptReplacementData.class);
                model.setSuccess(true);
                model.setAcceptReplacementData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    model.setSuccess(false);
                    model.setErrorMessage(response.body().getErrorMessages().get(0));
                }
            }
        } else {
            model.setSuccess(false);
            model.setErrorCode(response.code());
        }
        return model;
    }
}
