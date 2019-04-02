package com.tokopedia.discovery.newdiscovery.data.source;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.mapper.GuidedSearchMapper;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;

import rx.Observable;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchDataSource {

    private final BrowseApi searchApi;
    private final GuidedSearchMapper guidedSearchMapper;

    public GuidedSearchDataSource(BrowseApi searchApi,
                                  GuidedSearchMapper guidedSearchMapper) {
        this.searchApi = searchApi;
        this.guidedSearchMapper = guidedSearchMapper;
    }

    public Observable<GuidedSearchViewModel> getGuidedSearch(String keyword) {
        return searchApi.getGuidedSearch(keyword).map(guidedSearchMapper);
    }
}
