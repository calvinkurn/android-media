package com.tokopedia.seller.shopscore.data.factory;

import com.tokopedia.seller.shopscore.data.mapper.ShopScoreDetailMapper;
import com.tokopedia.seller.shopscore.data.source.ShopScoreDetailDataSource;
import com.tokopedia.seller.shopscore.data.source.ShopScoreSummaryDataSource;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.disk.ShopScoreCache;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreFactory {


    private final ShopScoreCloud shopScoreCloud;
    private final ShopScoreCache shopScoreCache;
    private final ShopScoreDetailMapper shopScoreDetailMapper;

    public ShopScoreFactory(
            ShopScoreCloud shopScoreCloud,
            ShopScoreCache shopScoreCache,
            ShopScoreDetailMapper shopScoreDetailMapper
    ) {
        this.shopScoreCloud = shopScoreCloud;
        this.shopScoreCache = shopScoreCache;
        this.shopScoreDetailMapper = shopScoreDetailMapper;
    }

    public ShopScoreSummaryDataSource createShopScoreSummarySource() {
        return new ShopScoreSummaryDataSource(shopScoreCloud, shopScoreCache);
    }

    public ShopScoreDetailDataSource createShopScoreDetailSource() {
        return new ShopScoreDetailDataSource(shopScoreCloud, shopScoreCache, shopScoreDetailMapper);
    }
}
