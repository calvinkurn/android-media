package com.tokopedia.seller.gmsubscribe.data.mapper.product;

import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GMProductServiceModel;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.model.GMServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModelGroup;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GmSubscribeProductMapper implements Func1<GMServiceModel, GmProductDomainModelGroup> {
    @Override
    public GmProductDomainModelGroup call(GMServiceModel gmServiceModel) {
        return mapServiceToDomain(gmServiceModel);
    }

    private GmProductDomainModelGroup mapServiceToDomain(GMServiceModel gmProductServiceModel) {
        GmProductDomainModelGroup group = new GmProductDomainModelGroup();
        group.setCurrentProduct(mapListServiceToDomain(gmProductServiceModel.getData().getProduct()));
        group.setExtendProduct(mapListServiceToDomain(gmProductServiceModel.getData().getExtend()));
        group.setPaymentMethod(gmProductServiceModel.getData().getPayMethod());
        return group;
    }

    private List<GmProductDomainModel> mapListServiceToDomain(List<GMProductServiceModel> serviceModels) {
        List<GmProductDomainModel> domainModels = new ArrayList<>();
        for (GMProductServiceModel serviceModel : serviceModels) {
            domainModels.add(mapModelServiceToDomain(serviceModel));
        }
        return domainModels;
    }

    private GmProductDomainModel mapModelServiceToDomain(GMProductServiceModel serviceModel) {
        GmProductDomainModel domain = new GmProductDomainModel();
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


