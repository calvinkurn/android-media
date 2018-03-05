package com.tokopedia.shop.sort.domain.repository;

import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopProductSortRepository {

    Observable<List<ShopProductSort>> getShopProductFilter();
}
