package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/12/17.
 */

public interface CatalogRepository {

    Observable<CatalogDomainModel> getBrowseCatalog(TKPDMapParam<String, Object> parameters);
}
