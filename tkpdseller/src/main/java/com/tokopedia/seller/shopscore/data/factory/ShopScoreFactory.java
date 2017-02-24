package com.tokopedia.seller.shopscore.data.factory;

import com.google.gson.Gson;
import com.tokopedia.seller.shopscore.data.source.ShopScoreDataSource;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class ShopScoreFactory {


    private final ShopScoreCloud shopScoreCloud;
    private final Gson gson;

    public ShopScoreFactory(ShopScoreCloud shopScoreCloud, Gson gson) {
        this.shopScoreCloud = shopScoreCloud;
        this.gson = gson;
    }

    public ShopScoreDataSource createShopScoreSource() {
        return new ShopScoreDataSource(shopScoreCloud, gson);
    }
}
