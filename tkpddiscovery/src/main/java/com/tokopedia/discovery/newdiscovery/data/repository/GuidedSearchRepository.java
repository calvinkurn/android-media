package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;

import rx.Observable;

/**
 * Created by HENRYPRIYONO on 14/02/18.
 */

public interface GuidedSearchRepository {
    Observable<GuidedSearchViewModel> getGuidedSearch(String keyword);
}
