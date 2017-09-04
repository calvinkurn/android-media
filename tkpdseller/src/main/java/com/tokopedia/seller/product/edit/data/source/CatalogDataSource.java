package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cloud.CatalogCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel;

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
