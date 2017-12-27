package com.tokopedia.seller.product.etalase.data.mapper;

import com.tokopedia.seller.product.edit.data.source.cloud.model.myetalase.EtalaseItem;
import com.tokopedia.seller.product.edit.data.source.cloud.model.myetalase.MyEtalaseListServiceModel;
import com.tokopedia.seller.product.etalase.domain.model.MyEtalaseDomainModel;
import com.tokopedia.seller.product.etalase.domain.model.MyEtalaseItemDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseServiceToDomainMapper implements Func1<MyEtalaseListServiceModel, MyEtalaseDomainModel> {
    @Override
    public MyEtalaseDomainModel call(MyEtalaseListServiceModel serviceModel) {
        MyEtalaseDomainModel domainModel = new MyEtalaseDomainModel();
        String uriNext = serviceModel.getData().getPaging().getUriNext();
        domainModel.setHasNext(uriNext != null && !uriNext.equals("0"));
        domainModel.setEtalaseItems(mapList(serviceModel.getData().getList()));
        return domainModel;
    }

    private List<MyEtalaseItemDomainModel> mapList(List<EtalaseItem> list) {
        List<MyEtalaseItemDomainModel> domainModels = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++){
            EtalaseItem item = list.get(i);
            MyEtalaseItemDomainModel domainModel = new MyEtalaseItemDomainModel();
            domainModel.setEtalaseId(item.getEtalaseId());
            domainModel.setEtalaseName(item.getEtalaseName());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

}
