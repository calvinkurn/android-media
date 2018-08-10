package com.tokopedia.inbox.common.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.common.data.pojo.UploadResponse;
import com.tokopedia.inbox.common.domain.model.UploadDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 30/07/18.
 */

public class UploadVideoMapper implements Func1<Response<ResolutionResponse<UploadResponse>>, UploadDomain> {

    @Inject
    public UploadVideoMapper() {
    }

    @Override
    public UploadDomain call(Response<ResolutionResponse<UploadResponse>> response) {
        return mappingResponse(response);
    }

    private UploadDomain mappingResponse(Response<ResolutionResponse<UploadResponse>> response) {
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
        UploadResponse uploadResponse = response.body().getData();
        return new UploadDomain(uploadResponse.getPicObj(),
                uploadResponse.getPicSrc(), true);
    }
}
