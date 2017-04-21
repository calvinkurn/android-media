package com.tokopedia.seller.product.domain;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopInfoRepository {
    Observable<ShopModel> getShopInfo(String userId, String deviceId, String shopId, String shopDomain);
    Observable<ShopModel> getShopInfoFromNetwork(String userId, String deviceId, String shopId, String shopDomain);
}
