package com.tokopedia.shop.info.domain.repository;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopInfoRepository {

    Observable<ShopInfo> getShopInfo(String shopId);
}
