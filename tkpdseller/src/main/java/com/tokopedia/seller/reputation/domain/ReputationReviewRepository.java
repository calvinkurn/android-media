package com.tokopedia.seller.reputation.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 3/16/17.
 */

public interface ReputationReviewRepository {
    Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param);

    Observable<ShopModel> getShopInfo(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam);

    Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams);

    Observable<ShopModel> getShopInfo(RequestParams requestParams);
}
