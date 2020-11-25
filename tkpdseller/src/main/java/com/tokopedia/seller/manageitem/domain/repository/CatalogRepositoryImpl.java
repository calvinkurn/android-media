package com.tokopedia.seller.manageitem.domain.repository;



import com.tokopedia.seller.manageitem.data.cloud.model.catalog.CatalogDataModel;
import com.tokopedia.seller.manageitem.data.source.CatalogDataSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */

public class CatalogRepositoryImpl implements CatalogRepository {
    private final CatalogDataSource catalogDataSource;

    @Inject
    public CatalogRepositoryImpl(CatalogDataSource catalogDataSource) {
        this.catalogDataSource = catalogDataSource;
    }

    @Override
    public Observable<CatalogDataModel> fetchCatalog(String keyword, long prodDeptId, int start, int row) {
        return catalogDataSource.fetchCatalog(keyword, prodDeptId, start, row);
    }
}
