package com.tokopedia.inbox.rescenter.discussion.data.mapper;


import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost.GenerateHostEntity;
import com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost.GeneratedHost;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostData;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostMapper implements Func1<Response<TkpdResponse>, GenerateHostModel> {
    @Override
    public GenerateHostModel call(Response<TkpdResponse> response) {
        GenerateHostModel domainData = new GenerateHostModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                GenerateHostEntity entity = response.body().convertDataObj(GenerateHostEntity.class);
                domainData.setSuccess(true);
                domainData.setGenerateHostData(mappingEntityDomain(entity.getGeneratedHost()));
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

    private GenerateHostData mappingEntityDomain(GeneratedHost generatedHost) {
        GenerateHostData generateHostData = new GenerateHostData();
        generateHostData.setServerId(generatedHost.getServerId());
        generateHostData.setUploadHost(generatedHost.getUploadHost());
        generateHostData.setUserId(generatedHost.getUserId());
        return generateHostData;

    }
}
