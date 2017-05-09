package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import android.util.Log;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.ResolutionActionDomainData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/22/17.
 */

public class ResolutionCenterActionMapper implements Func1<Response<TkpdResponse>, ResolutionActionDomainData> {

    @Override
    public ResolutionActionDomainData call(Response<TkpdResponse> response) {
        ResolutionActionDomainData domainModel = new ResolutionActionDomainData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                domainModel.setSuccess(true);
            } else {
                domainModel.setSuccess(false);
                domainModel.setMessageError(generateMessageError(response));
            }
        } else {
            domainModel.setSuccess(false);
            domainModel.setErrorCode(response.code());
        }
        return domainModel;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        String hangnadi = response.body().getErrorMessageJoined();
        Log.d("hangnadi.com", "generateMessageError: " + hangnadi);
        return response.body().getErrorMessageJoined();
    }
}
