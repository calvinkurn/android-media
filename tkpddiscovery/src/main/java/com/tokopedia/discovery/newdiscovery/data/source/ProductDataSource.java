package com.tokopedia.discovery.newdiscovery.data.source;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/5/17.
 */

public class ProductDataSource {

    private final ProductMapper productMapper;
    private final BrowseApi searchApi;

    public ProductDataSource(BrowseApi searchApi, ProductMapper productMapper) {
        this.searchApi = searchApi;
        this.productMapper = productMapper;
    }


    public Observable<SearchResultModel> getProduct(TKPDMapParam<String, Object> param) {
        return searchApi.browseProductsV3(param).map(productMapper);
    }

}
