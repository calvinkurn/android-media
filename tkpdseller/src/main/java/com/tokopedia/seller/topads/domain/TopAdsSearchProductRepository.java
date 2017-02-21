package com.tokopedia.seller.topads.domain;

import com.tokopedia.seller.topads.domain.model.ProductDomain;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public interface TopAdsSearchProductRepository {
    Observable<List<ProductDomain>> searchProduct(Map<String, String> param);
}
