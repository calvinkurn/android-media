package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.UploadResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/13/17.
 */

public class UploadVideoMapper implements Func1<Response<TkpdResponse>, UploadDomain> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";

    @Override
    public UploadDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private UploadDomain mappingResponse(Response<TkpdResponse> response) {
        UploadResponse uploadResponse = response.body().convertDataObj(UploadResponse.class);
        UploadDomain model = new UploadDomain(uploadResponse.getPicObj(),
                uploadResponse.getPicSrc(), true);
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(DEFAULT_ERROR);
                }
            } else {
                model.setSuccess(true);
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
