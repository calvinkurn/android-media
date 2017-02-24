package com.tokopedia.seller.shopscore.data.repository;

import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.source.ShopScoreDetailDataSource;
import com.tokopedia.seller.shopscore.data.source.ShopScoreSummaryDataSource;
import com.tokopedia.seller.shopscore.domain.ShopScoreRepository;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
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
    public Observable<ShopScoreMainDomainModel> getShopScoreSummary() {
        ShopScoreSummaryDataSource shopScoreSummaryDataSource = shopScoreFactory.createShopScoreSummarySource();
        return shopScoreSummaryDataSource.getShopScoreSummary();
    }

    @Override
    public Observable<ShopScoreDetailDomainModel> getShopScoreDetail() {
        ShopScoreDetailDataSource shopScoreDetailDataSource = shopScoreFactory.createShopScoreDetailSource();
        return shopScoreDetailDataSource.getShopScoreDetail();
    }
}
