package com.tokopedia.seller.shopscore.domain.model;

import java.util.List;

/**
 * Created by sebastianuskh on 3/2/17.
 */

public class ShopScoreDetailDomainModel {
    List<ShopScoreDetailItemDomainModel> itemModels;

    ShopScoreDetailSummaryDomainModel summaryModel;

    public List<ShopScoreDetailItemDomainModel> getItemModels() {
        return itemModels;
    }

    public void setItemModels(List<ShopScoreDetailItemDomainModel> itemModels) {
        this.itemModels = itemModels;
    }

    public ShopScoreDetailSummaryDomainModel getSummaryModel() {
        return summaryModel;
    }

    public void setSummaryModel(ShopScoreDetailSummaryDomainModel summaryModel) {
        this.summaryModel = summaryModel;
    }
}
