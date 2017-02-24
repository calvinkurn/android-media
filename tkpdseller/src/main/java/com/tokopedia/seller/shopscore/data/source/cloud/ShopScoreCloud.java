package com.tokopedia.seller.shopscore.data.source.cloud;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shopscore.data.source.cloud.api.ShopScoreApi;

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

    public Observable<String> getShopScoreMainData() {
        return api.getShopScoreSummary(sessionHandler.getShopID());
    }
}
