package com.tokopedia.discovery.imagesearch.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public interface ImageSearchRepository {

    Observable<SearchResultModel> getImageSearchResults(TKPDMapParam<String, Object> param);
}
