package com.tokopedia.inbox.rescenter.product.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.product.data.pojo.ListProductEntity;
import com.tokopedia.inbox.rescenter.product.data.pojo.ListProductItem;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductItemDomainData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListProductMapper implements Func1<Response<TkpdResponse>, ListProductDomainData> {

    @Override
    public ListProductDomainData call(Response<TkpdResponse> response) {
        ListProductDomainData domainData = new ListProductDomainData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ListProductEntity entity = response.body().convertDataObj(ListProductEntity.class);
                domainData.setSuccess(true);
                domainData.setListHistoryAddress(mappingEntityDomain(entity.getListComplainProduct()));
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

    private List<ListProductItemDomainData> mappingEntityDomain(List<ListProductItem> listProductItems) {
        List<ListProductItemDomainData> list = new ArrayList<>();
        for (ListProductItem item : listProductItems) {
            ListProductItemDomainData data = new ListProductItemDomainData();
            data.setProductImageUrl(item.getPhoto().getImageThumb());
            data.setResCenterProductID(item.getResProductId());
            data.setProductName(item.getName());
            list.add(data);
        }
        return list;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
