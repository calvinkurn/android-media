package com.tokopedia.seller.shopscore.domain.model;

import java.util.List;

/**
 * Created by sebastianuskh on 3/2/17.
 */

public class ShopScoreDetailDomainModel {
    public static final int GOLD_MERCHANT_QUALIFIED_BADGE = 5000;
    public static final int GOLD_MERCHANT_NOT_QUALIFIED_BADGE = 6000;
    public static final int NOT_GOLD_MERCHANT_QUALIFIED_BADGE = 7000;
    public static final int NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE = 8000;
    List<ShopScoreDetailItemDomainModel> itemModels;

    ShopScoreDetailSummaryDomainModel summaryModel;
    private int state;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
