package com.tokopedia.seller.shopscore.domain;

import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface ShopScoreRepository {
    Observable<ShopScoreMainDomainModel> getShopScoreSummary();

    Observable<ShopScoreDetailDomainModel> getShopScoreDetail();
}
