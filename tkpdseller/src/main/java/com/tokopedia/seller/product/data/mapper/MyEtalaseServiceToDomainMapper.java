package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.myetalase.EtalaseItem;
import com.tokopedia.seller.product.data.source.cloud.model.myetalase.MyEtalaseListServiceModel;
import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseServiceToDomainMapper implements Func1<MyEtalaseListServiceModel, List<MyEtalaseDomainModel>> {
    @Override
    public List<MyEtalaseDomainModel> call(MyEtalaseListServiceModel serviceModel) {
        List<MyEtalaseDomainModel> domainModels = new ArrayList<>();
        for(EtalaseItem list : serviceModel.getData().getList()){
            MyEtalaseDomainModel domainModel = new MyEtalaseDomainModel();
            domainModel.setEtalaseId(list.getEtalaseId());
            domainModel.setEtalaseName(list.getEtalaseName());
            domainModels.add(domainModel);
        }
        return domainModels;
    }
}
