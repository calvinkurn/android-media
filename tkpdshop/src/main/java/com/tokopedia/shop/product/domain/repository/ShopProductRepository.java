package com.tokopedia.shop.product.domain.repository;

import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopProductRepository {

    Observable<ShopNoteDetail> getShopProductList(HashMap<String, Object> hashMap, boolean shopClosed);
}
