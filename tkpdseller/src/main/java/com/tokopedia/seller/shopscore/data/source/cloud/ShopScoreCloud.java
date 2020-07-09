package com.tokopedia.seller.shopscore.data.source.cloud;

import com.tokopedia.core.network.apiservices.goldmerchant.apis.GoldMerchantApi;
import com.tokopedia.core.product.model.shopscore.detail.ShopScoreDetailServiceModel;
import com.tokopedia.core.product.model.shopscore.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.seller.shopscore.data.common.GetData;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreCloud {
    private final GoldMerchantApi api;
    private final UserSessionInterface userSession;

    @Inject
    public ShopScoreCloud(GoldMerchantApi api, UserSessionInterface userSession) {
        this.api = api;
        this.userSession = userSession;
    }

    public Observable<ShopScoreSummaryServiceModel> getShopScoreSummaryData() {
        return api
                .getShopScoreSummary(userSession.getShopId())
                .map(new GetData<ShopScoreSummaryServiceModel>());
    }

    public Observable<ShopScoreDetailServiceModel> getShopScoreDetailData() {
        return api
                .getShopScoreDetail(userSession.getShopId())
                .map(new GetData<ShopScoreDetailServiceModel>());
    }
}
