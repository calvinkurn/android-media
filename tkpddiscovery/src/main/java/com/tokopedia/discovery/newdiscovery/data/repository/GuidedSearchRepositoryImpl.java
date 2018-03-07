package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.discovery.newdiscovery.data.source.GuidedSearchDataSource;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;

import rx.Observable;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchRepositoryImpl implements GuidedSearchRepository {

    GuidedSearchDataSource guidedSearchDataSource;

    public GuidedSearchRepositoryImpl(GuidedSearchDataSource guidedSearchDataSource) {
        this.guidedSearchDataSource = guidedSearchDataSource;
    }

    @Override
    public Observable<GuidedSearchViewModel> getGuidedSearch(String keyword) {
        return guidedSearchDataSource.getGuidedSearch(keyword);
    }
}
