package com.tokopedia.seller.shopscore.domain;

import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface ShopScoreRepository {
    Observable<ShopScoreMainDomainModel> getShopScoreSummary();

    Observable<List<ShopScoreDetailDomainModel>> getShopScoreDetail();
}
