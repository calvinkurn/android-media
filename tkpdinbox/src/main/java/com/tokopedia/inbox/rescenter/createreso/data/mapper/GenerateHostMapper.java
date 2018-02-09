package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.GenerateHostResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.network.ErrorMessageException.DEFAULT_ERROR;

/**
 * Created by yoasfs on 05/09/17.
 */

public class GenerateHostMapper implements Func1<Response<TkpdResponse>, GenerateHostDomain> {

    @Override
    public GenerateHostDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private GenerateHostDomain mappingResponse(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

        GenerateHostResponse generateHostResponse =
                response.body().convertDataObj(GenerateHostResponse.class);
        return new GenerateHostDomain(generateHostResponse.getServerId(),
                generateHostResponse.getUploadHost(),
                generateHostResponse.getToken());
    }
}
