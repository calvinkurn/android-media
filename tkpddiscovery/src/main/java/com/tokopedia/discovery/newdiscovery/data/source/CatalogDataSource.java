package com.tokopedia.discovery.newdiscovery.data.source;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.mapper.BrowseCatalogMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogDataSource {

    private final BrowseApi browseApi;
    private final BrowseCatalogMapper browseCatalogMapper;

    public CatalogDataSource(BrowseApi browseApi, BrowseCatalogMapper browseCatalogMapper) {
        this.browseApi = browseApi;
        this.browseCatalogMapper = browseCatalogMapper;
    }

    public Observable<CatalogDomainModel> getBrowseCatalog(TKPDMapParam<String, Object> parameters) {
        return browseApi.browseCatalogRevamp(parameters).map(browseCatalogMapper);
    }
}
