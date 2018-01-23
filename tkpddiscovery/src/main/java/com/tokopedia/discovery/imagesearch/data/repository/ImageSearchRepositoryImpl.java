package com.tokopedia.discovery.imagesearch.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.imagesearch.data.source.ImageSearchDataSource;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchRepositoryImpl implements ImageSearchRepository {


    private final ImageSearchDataSource imageSearchDataSource;

    public ImageSearchRepositoryImpl(ImageSearchDataSource imageSearchDataSource) {
        this.imageSearchDataSource = imageSearchDataSource;
    }

    @Override
    public Observable<SearchResultModel> getImageSearchResults(TKPDMapParam<String, Object> param) {
        return imageSearchDataSource.getImageSearch(param);
    }

}
