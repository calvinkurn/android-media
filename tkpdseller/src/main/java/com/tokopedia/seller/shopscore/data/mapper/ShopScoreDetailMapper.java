package com.tokopedia.seller.shopscore.data.mapper;

import android.support.annotation.NonNull;

import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailItemServiceModel;
import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailItemDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailSummaryDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailMapper implements Func1<ShopScoreDetailServiceModel, ShopScoreDetailDomainModel> {
    @Override
    public ShopScoreDetailDomainModel call(ShopScoreDetailServiceModel serviceModel) {
        ShopScoreDetailDomainModel dataDomain = new ShopScoreDetailDomainModel();

        List<ShopScoreDetailItemDomainModel> dataDomainItemModels = mapShopScoreDetailItemDomainModels(serviceModel);
        dataDomain.setItemModels(dataDomainItemModels);

        ShopScoreDetailSummaryDomainModel summaryModel = mapShopScoreDetailSummaryDomainModels(serviceModel);
        dataDomain.setSummaryModel(summaryModel);

        return dataDomain;
    }

    private ShopScoreDetailSummaryDomainModel mapShopScoreDetailSummaryDomainModels(ShopScoreDetailServiceModel serviceModel) {
        ShopScoreDetailSummaryDomainModel summaryDomainModel = new ShopScoreDetailSummaryDomainModel();
        summaryDomainModel.setBadgeScore(serviceModel.getData().getBadgeScore());
        summaryDomainModel.setValue(serviceModel.getData().getSumData().getValue());
        String color = serviceModel.getData().getSumData().getColor();
        summaryDomainModel.setColor(ColorUtil.formatColor(color));
        summaryDomainModel.setHtml(serviceModel.getData().getSumData().getHtml());
        summaryDomainModel.setText(serviceModel.getData().getSumData().getText());
        return summaryDomainModel;
    }

    @NonNull
    private List<ShopScoreDetailItemDomainModel> mapShopScoreDetailItemDomainModels(ShopScoreDetailServiceModel serviceModel) {
        List<ShopScoreDetailItemDomainModel> dataDomainItemModels = new ArrayList<>();
        for (ShopScoreDetailItemServiceModel data : serviceModel.getData().getData()) {
            ShopScoreDetailItemDomainModel dataServiceModel = dataDomainModelMap(data);
            dataDomainItemModels.add(dataServiceModel);
        }
        return dataDomainItemModels;
    }

    private ShopScoreDetailItemDomainModel dataDomainModelMap(ShopScoreDetailItemServiceModel data) {
        ShopScoreDetailItemDomainModel domainModel = new ShopScoreDetailItemDomainModel();
        domainModel.setTitle(data.getTitle());
        domainModel.setValue(data.getValue());
        domainModel.setDescription(data.getDescription());
        domainModel.setProgressBarColor(ColorUtil.formatColor(data.getColor()));
        return domainModel;
    }
}
