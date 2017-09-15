package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.topads.dashboard.domain.model.ProductListDomain;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public interface TopAdsSearchProductDataSource {
    Observable<ProductListDomain> searchProduct(Map<String, String> param);
}
