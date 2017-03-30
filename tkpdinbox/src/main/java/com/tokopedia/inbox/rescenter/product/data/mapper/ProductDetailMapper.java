package com.tokopedia.inbox.rescenter.product.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.product.data.pojo.ProductDetailEntity;
import com.tokopedia.inbox.rescenter.product.domain.model.AttachmentProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ProductDetailData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailMapper implements Func1<Response<TkpdResponse>, ProductDetailData> {

    @Override
    public ProductDetailData call(Response<TkpdResponse> response) {
        ProductDetailData domainData = new ProductDetailData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ProductDetailEntity entity = response.body().convertDataObj(ProductDetailEntity.class);
                domainData.setSuccess(true);
                domainData.setProductName(entity.getProduct().getName());
                domainData.setProductPrice("NOT SET");
                domainData.setProductThumbUrl(entity.getProduct().getPhoto().getImageThumb());
                domainData.setTrouble(entity.getTrouble().getName());
                domainData.setTroubleReason(entity.getRemark());
                domainData.setAttachment(
                        entity.getAttachments() != null && !entity.getAttachments().isEmpty() ?
                                mappingAttachment(entity.getAttachments()) : null
                );
            } else {
                domainData.setSuccess(false);
                domainData.setMessageError(generateMessageError(response));
            }
        } else {
            domainData.setSuccess(false);
            domainData.setErrorCode(response.code());
        }
        return domainData;
    }

    private List<AttachmentProductDomainData> mappingAttachment(List<ProductDetailEntity.Attachments> attachments) {
        List<AttachmentProductDomainData> list = new ArrayList<>();
        for (ProductDetailEntity.Attachments item : attachments) {
            AttachmentProductDomainData data = new AttachmentProductDomainData();
            data.setThumbUrl(item.getImageThumb());
            data.setUrl(item.getUrl());
            list.add(data);
        }
        return list;
    }


    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
