package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.UploadResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class UploadMapper implements Func1<Response<TkpdResponse>, UploadDomain> {

    @Override
    public UploadDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadDomain mappingResponse(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        UploadResponse uploadResponse = response.body().convertDataObj(UploadResponse.class);
        return new UploadDomain(uploadResponse.getPicObj(),
                uploadResponse.getPicSrc(), false);
    }
}
