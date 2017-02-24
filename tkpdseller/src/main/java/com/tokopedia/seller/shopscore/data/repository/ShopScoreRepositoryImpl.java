package com.tokopedia.seller.shopscore.data.repository;

import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.source.ShopScoreDataSource;
import com.tokopedia.seller.shopscore.domain.ShopScoreRepository;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */

public class ShopScoreRepositoryImpl implements ShopScoreRepository {
    private final ShopScoreFactory shopScoreFactory;

    public ShopScoreRepositoryImpl(ShopScoreFactory shopScoreFactory) {
        this.shopScoreFactory = shopScoreFactory;
    }

    @Override
    public Observable<ShopScoreMainDomainModel> getShopScoreMainData() {
        ShopScoreDataSource shopScoreDataSource = shopScoreFactory.createShopScoreSource();
        return shopScoreDataSource.getShopScoreMainData();
    }
}
