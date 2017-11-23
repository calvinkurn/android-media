package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.source.ProductDataSource;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * @author by errysuprayogi on 10/13/17.
 */

public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDataSource productDataSource;

    public ProductRepositoryImpl(ProductDataSource productDataSource) {
        this.productDataSource = productDataSource;
    }

    @Override
    public Observable<SearchResultModel> getProduct(TKPDMapParam<String, Object> param) {
        return productDataSource.getProduct(param);
    }

    @Override
    public void getProductLoadMore() {

    }
}
