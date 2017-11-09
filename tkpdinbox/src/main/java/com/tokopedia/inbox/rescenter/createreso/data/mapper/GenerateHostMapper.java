package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.GenerateHostResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class GenerateHostMapper implements Func1<Response<TkpdResponse>, GenerateHostDomain> {

    @Override
    public GenerateHostDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private GenerateHostDomain mappingResponse(Response<TkpdResponse> response) {
        GenerateHostResponse generateHostResponse =
                response.body().convertDataObj(GenerateHostResponse.class);
        GenerateHostDomain model = new GenerateHostDomain(generateHostResponse.getServerId(),
                generateHostResponse.getUploadHost(),
                generateHostResponse.getToken());
        if (response.isSuccessful()) {
            if (response.body().isNullData()) {
                if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
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
