package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import android.util.Log;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterMapper implements Func1<Response<TkpdResponse>, DetailResCenter> {

    public DetailResCenterMapper() {
    }

    @Override
    public DetailResCenter call(Response<TkpdResponse> response) {
        DetailResCenter domainModel = new DetailResCenter();
        if (response.isSuccessful()) {
            if (response.body().isError()) {
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
        return response.body().getErrorMessageJoined();
    }
}
