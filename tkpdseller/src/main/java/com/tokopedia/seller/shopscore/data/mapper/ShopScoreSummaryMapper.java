package com.tokopedia.seller.shopscore.data.mapper;

import com.tokopedia.core.product.model.shopscore.summary.Data;
import com.tokopedia.core.product.model.shopscore.summary.DetailData;
import com.tokopedia.core.product.model.shopscore.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreSummaryDomainModelData;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreSummaryMapper implements Func1<ShopScoreSummaryServiceModel, ShopScoreMainDomainModel> {
    @Override
    public ShopScoreMainDomainModel call(ShopScoreSummaryServiceModel serviceModel) {
        Data serviceModelData = serviceModel.getData();

        ShopScoreMainDomainModel domainModel = new ShopScoreMainDomainModel();
        domainModel.setBadgeScore(serviceModelData.getBadgeScore());

        ShopScoreSummaryDomainModelData data = new ShopScoreSummaryDomainModelData();
        DetailData detailData = serviceModelData.getData();
        data.setTitle(detailData.getTitle());
        data.setValue(detailData.getValue());
        data.setProgressBarColor(ColorUtil.formatColorWithAlpha(detailData.getColor()));
        data.setDescription(detailData.getDescription());

        domainModel.setData(data);

        return domainModel;
    }
}
