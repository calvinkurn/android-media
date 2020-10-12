package com.tokopedia.seller.manageitem.data.source;


import com.tokopedia.seller.manageitem.data.cloud.CatalogCloud;
import com.tokopedia.seller.manageitem.data.cloud.model.catalog.CatalogDataModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CatalogDataSource {
    private final CatalogCloud catalogCloud;

    @Inject
    public CatalogDataSource(CatalogCloud catalogCloud) {
        this.catalogCloud = catalogCloud;
    }

    public Observable<CatalogDataModel> fetchCatalog(String keyword, long prodDeptId, int start, int row) {
        return catalogCloud.fetchData(keyword, prodDeptId, start, row);
    }
}
