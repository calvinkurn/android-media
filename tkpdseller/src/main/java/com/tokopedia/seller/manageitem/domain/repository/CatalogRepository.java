package com.tokopedia.seller.manageitem.domain.repository;


import com.tokopedia.seller.manageitem.data.cloud.model.catalog.CatalogDataModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CatalogRepository {
    Observable<CatalogDataModel> fetchCatalog(String keyword, long prodDeptId, int start, int rows);
}
