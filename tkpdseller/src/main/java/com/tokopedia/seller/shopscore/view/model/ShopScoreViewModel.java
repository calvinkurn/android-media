package com.tokopedia.seller.shopscore.view.model;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreViewModel {
    private int badgeScore;
    private ShopScoreViewModelData data;

    public int getBadgeScore() {
        return badgeScore;
    }

    public void setBadgeScore(int badgeScore) {
        this.badgeScore = badgeScore;
    }

    public ShopScoreViewModelData getData() {
        return data;
    }

    public void setData(ShopScoreViewModelData data) {
        this.data = data;
    }
}
