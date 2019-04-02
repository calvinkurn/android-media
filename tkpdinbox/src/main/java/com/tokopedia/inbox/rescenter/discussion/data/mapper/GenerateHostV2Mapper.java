package com.tokopedia.inbox.rescenter.discussion.data.mapper;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost.GenerateHostV2Entity;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 7/12/17.
 */

public class GenerateHostV2Mapper implements Func1<Response<TkpdResponse>, GenerateHostModel> {

    @Override
    public GenerateHostModel call(Response<TkpdResponse> response) {
        GenerateHostModel domainData = new GenerateHostModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                GenerateHostV2Entity entity = response.body().convertDataObj(GenerateHostV2Entity.class);
                domainData.setSuccess(true);
                domainData.setGenerateHostData(mappingEntityDomain(entity));
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    domainData.setSuccess(false);
                } else {
                    throw new MessageErrorException(response.body().getErrorMessageJoined());
                }
            }
            domainData.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return domainData;
    }

    private GenerateHostData mappingEntityDomain(GenerateHostV2Entity entity) {
        GenerateHostData generateHostData = new GenerateHostData();
        generateHostData.setServerId(entity.getServerId());
        generateHostData.setUploadHost(entity.getUploadHost());
        generateHostData.setToken(entity.getToken());
        return generateHostData;

    }

}
