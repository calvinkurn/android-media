package com.tokopedia.discovery.imagesearch.data.source;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchDataSource {

    private final ProductMapper productMapper;
    private final BrowseApi searchApi;

    public ImageSearchDataSource(ProductMapper productMapper, BrowseApi browseApi) {
        this.productMapper = productMapper;
        this.searchApi = browseApi;
    }


    public Observable<SearchResultModel> getImageSearch(TKPDMapParam<String, Object> param) {

        return searchApi.browseProductsV3(param).map(productMapper);

    }
}
