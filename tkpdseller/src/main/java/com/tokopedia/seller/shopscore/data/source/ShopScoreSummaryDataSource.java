package com.tokopedia.seller.shopscore.data.source;

import com.google.gson.Gson;
import com.tokopedia.seller.shopscore.data.mapper.ShopScoreSummaryMapper;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreSummaryDataSource {
    private final ShopScoreCloud shopScoreCloud;
    private final Gson gson;

    public ShopScoreSummaryDataSource(ShopScoreCloud shopScoreCloud, Gson gson) {
        this.shopScoreCloud = shopScoreCloud;
        this.gson = gson;
    }

    public Observable<ShopScoreMainDomainModel> getShopScoreSummary() {
        return shopScoreCloud
                .getShopScoreSummaryData()
                .map(new ShopScoreSummaryMapper());
    }
}
