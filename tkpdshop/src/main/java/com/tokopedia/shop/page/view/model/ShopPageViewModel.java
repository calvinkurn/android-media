package com.tokopedia.shop.page.view.model;

import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

/**
 * Created by nathan on 2/24/18.
 */

public class ShopPageViewModel {

    private ShopInfo shopInfo;
    private ReputationSpeed reputationSpeed;

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public ReputationSpeed getReputationSpeed() {
        return reputationSpeed;
    }

    public void setReputationSpeed(ReputationSpeed reputationSpeed) {
        this.reputationSpeed = reputationSpeed;
    }
}
