package com.tokopedia.seller.gmsubscribe.data.mapper.product;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GMProductServiceModel;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GMServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModelGroup;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMSubscribeProductMapper implements Func1<GMServiceModel, GMProductDomainModelGroup> {
    @Override
    public GMProductDomainModelGroup call(GMServiceModel gmServiceModel) {
        return mapServiceToDomain(gmServiceModel);
    }

    private GMProductDomainModelGroup mapServiceToDomain(GMServiceModel gmProductServiceModel) {
        GMProductDomainModelGroup group = new GMProductDomainModelGroup();
        group.setCurrentProduct(mapListServiceToDomain(gmProductServiceModel.getData().getProduct()));
        group.setExtendProduct(mapListServiceToDomain(gmProductServiceModel.getData().getExtend()));
        group.setPaymentMethod(gmProductServiceModel.getData().getPayMethod());
        return group;
    }

    private List<GMProductDomainModel> mapListServiceToDomain(List<GMProductServiceModel> serviceModels) {
        List<GMProductDomainModel> domainModels = new ArrayList<>();
        for (GMProductServiceModel serviceModel : serviceModels) {
            domainModels.add(mapModelServiceToDomain(serviceModel));
        }
        return domainModels;
    }

    private GMProductDomainModel mapModelServiceToDomain(GMProductServiceModel serviceModel) {
        GMProductDomainModel domain = new GMProductDomainModel();
        domain.setProductId(serviceModel.getProductId());
        domain.setName(serviceModel.getProductName());
        domain.setNotes(serviceModel.getNotes());
        domain.setPrice(serviceModel.getPriceFmt());
        domain.setBestDeal(serviceModel.getBestDeal() == 1);
        domain.setNextInv(serviceModel.getNextInv());
        domain.setFreeDays(serviceModel.getFreeDays());
        domain.setLastPrice(serviceModel.getLastPriceFmt());
        return domain;
    }


}


