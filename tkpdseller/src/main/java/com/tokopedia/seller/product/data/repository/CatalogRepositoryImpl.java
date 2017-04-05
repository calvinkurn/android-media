package com.tokopedia.seller.product.data.repository;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.data.source.CatalogDataSource;
import com.tokopedia.seller.product.domain.CatalogRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/8/17.
 */
@ActivityScope
public class CatalogRepositoryImpl implements CatalogRepository {
    private final CatalogDataSource catalogDataSource;

    @Inject
    public CatalogRepositoryImpl(CatalogDataSource catalogDataSource) {
        this.catalogDataSource = catalogDataSource;
    }

    @Override
    public Observable<CatalogDataModel> fetchCatalog(String keyword, int prodDeptId, int start, int row) {
        return catalogDataSource.fetchCatalog(keyword, prodDeptId, start, row);
    }
}
