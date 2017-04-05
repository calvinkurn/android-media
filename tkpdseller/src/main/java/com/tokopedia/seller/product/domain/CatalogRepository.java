package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CatalogRepository {
    Observable<CatalogDataModel> fetchCatalog(String keyword, int prodDeptId, int start, int rows);
}
