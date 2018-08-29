package com.tokopedia.inbox.common.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.common.data.pojo.GenerateHostDataResponse;
import com.tokopedia.inbox.common.data.pojo.GenerateHostResponse;
import com.tokopedia.inbox.common.domain.model.GenerateHostDomain;
import com.tokopedia.inbox.rescenter.network.ResolutionResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 30/07/18.
 */

public class GenerateHostMapper implements Func1<Response<ResolutionResponse<GenerateHostDataResponse>>, GenerateHostDomain> {

    @Inject
    public GenerateHostMapper() {
    }

    @Override
    public GenerateHostDomain call(Response<ResolutionResponse<GenerateHostDataResponse>> response) {
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
