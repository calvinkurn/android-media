package com.tokopedia.seller.product.data.source;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.cloud.CatalogCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/4/17.
 */

@ActivityScope
public class CatalogDataSource {
    private final CatalogCloud catalogCloud;

    @Inject
    public CatalogDataSource(CatalogCloud catalogCloud) {
        this.catalogCloud = catalogCloud;
    }

    public Observable<CatalogDataModel> fetchCatalog(
                        String keyword, int prodDeptId, int start, int row) {
        return catalogCloud.fetchData(keyword, prodDeptId, start, row);
    }


}
