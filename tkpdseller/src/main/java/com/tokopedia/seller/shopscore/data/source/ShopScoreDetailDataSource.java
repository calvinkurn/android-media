package com.tokopedia.seller.shopscore.data.source;

import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.cloud.model.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDataSource {
    private final ShopScoreCloud shopScoreCloud;

    public ShopScoreDetailDataSource(ShopScoreCloud shopScoreCloud) {
        this.shopScoreCloud = shopScoreCloud;
    }

    public Observable<ShopScoreDetailDomainModel> getShopScoreDetail() {
        return shopScoreCloud.getShopScoreDetailData()
                .map(new ServiceModelCreator())
                .map(new ShopScoreDetailMapper());
    }

    private class ServiceModelCreator implements Func1<String, ShopScoreDetailServiceModel> {
        @Override
        public ShopScoreDetailServiceModel call(String s) {
            return null;
        }
    }
}
