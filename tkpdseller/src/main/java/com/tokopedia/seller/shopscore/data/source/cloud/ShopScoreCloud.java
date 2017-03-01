package com.tokopedia.seller.shopscore.data.source.cloud;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shopscore.data.common.GetData;
import com.tokopedia.seller.shopscore.data.source.cloud.api.ShopScoreApi;
import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.data.source.cloud.model.summary.ShopScoreSummaryServiceModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreCloud {
    private final ShopScoreApi api;
    private final SessionHandler sessionHandler;

    public ShopScoreCloud(ShopScoreApi api, SessionHandler sessionHandler) {
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
