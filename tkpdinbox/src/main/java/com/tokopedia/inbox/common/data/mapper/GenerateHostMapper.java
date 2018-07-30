package com.tokopedia.inbox.common.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.common.data.pojo.GenerateHostResponse;
import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 30/07/18.
 */

public class GenerateHostMapper implements Func1<Response<DataResponse<GenerateHostResponse>>, GenerateHostDomain> {

    @Inject
    public GenerateHostMapper() {
    }

    @Override
    public GenerateHostDomain call(Response<DataResponse<GenerateHostResponse>> response) {
        return mappingResponse(response);
    }

    private GenerateHostDomain mappingResponse(Response<DataResponse<GenerateHostResponse>> response) {
        GenerateHostResponse generateHostResponse =
                response.body().getData();
        return new GenerateHostDomain(generateHostResponse.getServerId(),
                generateHostResponse.getUploadHost(),
                generateHostResponse.getToken());
    }
}
