package com.tokopedia.seller.shopscore.data.source;

import com.tokopedia.core.product.model.shopscore.summary.ShopScoreSummaryServiceModel;
import com.tokopedia.seller.shopscore.data.mapper.ShopScoreSummaryMapper;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.disk.ShopScoreCache;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreSummaryDataSource {
    private final ShopScoreCloud shopScoreCloud;
    private final ShopScoreCache shopScoreCache;

    public ShopScoreSummaryDataSource(ShopScoreCloud shopScoreCloud, ShopScoreCache shopScoreCache) {
        this.shopScoreCloud = shopScoreCloud;
        this.shopScoreCache = shopScoreCache;
    }

    public Observable<ShopScoreMainDomainModel> getShopScoreSummary() {
        return shopScoreCache.getShopScoreSummary()
                .onErrorResumeNext(
                        shopScoreCloud.getShopScoreSummaryData()
                                .doOnNext(new StoreShopScoreSummaryToCache())
                )
                .map(new ShopScoreSummaryMapper());

    }

    private class StoreShopScoreSummaryToCache implements Action1<ShopScoreSummaryServiceModel> {
        @Override
        public void call(ShopScoreSummaryServiceModel serviceModel) {
            shopScoreCache.storeShopScoreSummary(serviceModel);
        }
    }
}
