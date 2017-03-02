package com.tokopedia.seller.shopscore.data.source;

import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.data.source.disk.ShopScoreCache;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDataSource {
    private final ShopScoreCloud shopScoreCloud;
    private final ShopScoreCache shopScoreCache;

    public ShopScoreDetailDataSource(ShopScoreCloud shopScoreCloud, ShopScoreCache shopScoreCache) {
        this.shopScoreCloud = shopScoreCloud;
        this.shopScoreCache = shopScoreCache;
    }

    public Observable<ShopScoreDetailDomainModel> getShopScoreDetail() {
        return shopScoreCache.getShopScoreDetail()
                .onErrorResumeNext(
                        shopScoreCloud.getShopScoreDetailData()
                                .doOnNext(new StoreShopDetailToCache())
                )
                .map(new ShopScoreDetailMapper());
    }

    private class StoreShopDetailToCache implements Action1<ShopScoreDetailServiceModel> {
        @Override
        public void call(ShopScoreDetailServiceModel serviceModel) {
            shopScoreCache.storeShopScoreDetail(serviceModel);
        }
    }
}
