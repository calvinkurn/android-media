package com.tokopedia.seller.shopscore.data.mapper;

import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailDataServiceModel;
import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailMapper implements Func1<ShopScoreDetailServiceModel, List<ShopScoreDetailDomainModel>> {
    @Override
    public List<ShopScoreDetailDomainModel> call(ShopScoreDetailServiceModel serviceModel) {
        List<ShopScoreDetailDomainModel> dataDomainModels = new ArrayList<>();
        for (ShopScoreDetailDataServiceModel data : serviceModel.getData()) {
            ShopScoreDetailDomainModel dataServiceModel = dataDomainModelMap(data);
            dataDomainModels.add(dataServiceModel);
        }
        return dataDomainModels;
    }

    private ShopScoreDetailDomainModel dataDomainModelMap(ShopScoreDetailDataServiceModel data) {
        ShopScoreDetailDomainModel domainModel = new ShopScoreDetailDomainModel();
        domainModel.setTitle(data.getTitle());
        domainModel.setValue(data.getValue());
        domainModel.setDescription(data.getDescription());
        return domainModel;
    }
}
