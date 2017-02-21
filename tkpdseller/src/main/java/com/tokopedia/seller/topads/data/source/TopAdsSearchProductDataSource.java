package com.tokopedia.seller.topads.data.source;

import com.tokopedia.seller.topads.domain.model.ProductDomain;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public interface TopAdsSearchProductDataSource {
    Observable<List<ProductDomain>> searchProduct(Map<String, String> param);
}
