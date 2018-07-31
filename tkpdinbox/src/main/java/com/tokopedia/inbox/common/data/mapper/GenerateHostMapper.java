package com.tokopedia.inbox.common.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.inbox.common.data.pojo.GenerateHostDataResponse;
import com.tokopedia.inbox.common.data.pojo.GenerateHostResponse;
import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 30/07/18.
 */

public class GenerateHostMapper implements Func1<Response<DataResponse<GenerateHostDataResponse>>, GenerateHostDomain> {

    @Inject
    public GenerateHostMapper() {
    }

    @Override
    public GenerateHostDomain call(Response<DataResponse<GenerateHostDataResponse>> response) {
        GenerateHostResponse generateHostResponse =
                response.body().getData().getGenerateHostResponse();
        return mappingResponse(generateHostResponse);
    }

    private GenerateHostDomain mappingResponse(GenerateHostResponse response) {

        return new GenerateHostDomain(response.getServerId(),
                response.getUploadHost(),
                response.getToken());
    }
}
