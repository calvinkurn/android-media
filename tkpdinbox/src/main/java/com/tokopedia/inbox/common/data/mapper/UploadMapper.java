package com.tokopedia.inbox.common.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.common.data.pojo.UploadResponse;
import com.tokopedia.inbox.common.domain.model.UploadDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 30/07/18.
 */

public class UploadMapper implements Func1<Response<DataResponse<UploadResponse>>, UploadDomain> {

    @Inject
    public UploadMapper() {
    }

    @Override
    public UploadDomain call(Response<DataResponse<UploadResponse>> response) {
        return mappingResponse(response);
    }

    private UploadDomain mappingResponse(Response<DataResponse<UploadResponse>> response) {
        UploadResponse uploadResponse = response.body().getData();
        return new UploadDomain(uploadResponse.getPicObj(),
                uploadResponse.getPicSrc(), false);
    }
}
