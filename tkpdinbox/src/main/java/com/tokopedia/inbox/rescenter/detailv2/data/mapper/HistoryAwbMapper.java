package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.history.domain.model.HistoryAwbData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAwbMapper implements Func1<Response<TkpdResponse>, HistoryAwbData> {

    @Override
    public HistoryAwbData call(Response<TkpdResponse> response) {
        HistoryAwbData domainData = new HistoryAwbData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
//                DetailResCenterEntity entity
//                        = response.body().convertDataObj(DetailResCenterEntity.class);
                domainData.setSuccess(true);
            } else {
                domainData.setSuccess(false);
                domainData.setMessageError(generateMessageError(response));
            }
        } else {
            domainData.setSuccess(false);
            domainData.setErrorCode(response.code());
        }
        return domainData;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
