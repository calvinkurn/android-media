package com.tokopedia.seller.shopscore.data.source.cloud;

import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.product.model.shopscore.detail.ShopScoreDetailServiceModel;
import com.tokopedia.core.product.model.shopscore.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shopscore.data.common.GetData;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreCloud {
    private final GoldMerchantApi api;
    private final SessionHandler sessionHandler;

    @Inject
    public ShopScoreCloud(GoldMerchantApi api, SessionHandler sessionHandler) {
        this.api = api;
        this.sessionHandler = sessionHandler;
    }

    public Observable<ShopScoreSummaryServiceModel> getShopScoreSummaryData() {
        return api
                .getShopScoreSummary(sessionHandler.getShopID())
                .map(new GetData<ShopScoreSummaryServiceModel>());
    }

    public Observable<ShopScoreDetailServiceModel> getShopScoreDetailData() {
        return api
                .getShopScoreDetail(sessionHandler.getShopID())
                .map(new GetData<ShopScoreDetailServiceModel>());
    }
}
