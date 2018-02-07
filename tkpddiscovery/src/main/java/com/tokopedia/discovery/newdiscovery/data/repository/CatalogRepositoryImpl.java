package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.source.CatalogDataSource;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class CatalogRepositoryImpl implements CatalogRepository {

    private final CatalogDataSource catalogDataSrouce;

    public CatalogRepositoryImpl(CatalogDataSource catalogDataSrouce) {
        this.catalogDataSrouce = catalogDataSrouce;
    }

    @Override
    public Observable<CatalogDomainModel> getBrowseCatalog(TKPDMapParam<String, Object> parameters) {
        return catalogDataSrouce.getBrowseCatalog(parameters);
    }
}
