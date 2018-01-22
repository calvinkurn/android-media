package com.tokopedia.discovery.imagesearch.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchResultModel;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public interface ImageSearchRepository {

    Observable<ImageSearchResultModel> getImageSearchResults( RequestParams param);
}
