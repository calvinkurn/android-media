package com.tokopedia.seller.product.data.mapper;


import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHostModel;
import com.tokopedia.seller.product.domain.model.GenerateHostDomainModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class GenerateHostMapper implements Func1<GenerateHost, GenerateHostDomainModel> {

    @Inject
    public GenerateHostMapper() {
    }

    @Override
    public GenerateHostDomainModel call(GenerateHost generateHostModelResponse) {
        GenerateHostDomainModel domainModel = new GenerateHostDomainModel();
        domainModel.setServerId(Integer.parseInt(generateHostModelResponse.getServerId()));
        domainModel.setUrl(generateHostModelResponse.getUploadHost());
        return domainModel;
    }
}
