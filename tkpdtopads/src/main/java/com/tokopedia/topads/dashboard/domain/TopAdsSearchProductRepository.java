package com.tokopedia.seller.topads.dashboard.domain;

import com.tokopedia.seller.topads.dashboard.domain.model.ProductListDomain;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public interface TopAdsSearchProductRepository {
    Observable<ProductListDomain> searchProduct(Map<String, String> param);
}
