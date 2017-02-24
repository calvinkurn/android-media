package com.tokopedia.seller.shopscore.data.mapper;

import com.tokopedia.seller.shopscore.data.source.cloud.model.Data;
import com.tokopedia.seller.shopscore.data.source.cloud.model.DetailData;
import com.tokopedia.seller.shopscore.data.source.cloud.model.ShopScoreMainDataServiceModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreSummaryDomainModelData;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreMainMapper implements Func1<ShopScoreMainDataServiceModel, ShopScoreMainDomainModel> {
    @Override
    public ShopScoreMainDomainModel call(ShopScoreMainDataServiceModel serviceModel) {
        Data serviceModelData = serviceModel.getData();

        ShopScoreMainDomainModel domainModel = new ShopScoreMainDomainModel();
        domainModel.setBadgeScore(serviceModelData.getBadgeScore());

        ShopScoreSummaryDomainModelData data = new ShopScoreSummaryDomainModelData();
        DetailData detailData = serviceModelData.getData();
        data.setTitle(detailData.getTitle());
        data.setValue(detailData.getValue());

        data.setDescription(detailData.getDescription());

        domainModel.setData(data);

        return domainModel;
    }
}
