package com.tokopedia.seller.shopscore.data.source;

import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailDataSource {
    private final ShopScoreCloud shopScoreCloud;

    public ShopScoreDetailDataSource(ShopScoreCloud shopScoreCloud) {
        this.shopScoreCloud = shopScoreCloud;
    }

    public Observable<List<ShopScoreDetailDomainModel>> getShopScoreDetail() {
        return shopScoreCloud.getShopScoreDetailData()
                .map(new ShopScoreDetailMapper());
    }
}
