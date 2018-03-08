package com.tokopedia.inbox.attachproduct.data.model.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.attachproduct.data.model.AttachProductAPIResponseWrapper;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 08/03/18.
 */

public class TkpdResponseToAttachProductDomainModelMapper implements Func1<Response<TkpdResponse>, AttachProductDomainModel> {
    @Override
    public AttachProductDomainModel call(Response<TkpdResponse> tkpdResponseResponse) {
        AttachProductDomainModel domainModel = new AttachProductDomainModel();
        TkpdResponse tkpdResponse = tkpdResponseResponse.body();
        AttachProductAPIResponseWrapper attachProductAPIResponseWrapper = tkpdResponse.convertDataObj(AttachProductAPIResponseWrapper.class);
        domainModel.setProducts(attachProductAPIResponseWrapper.getProducts());
        return domainModel;
    }
}
