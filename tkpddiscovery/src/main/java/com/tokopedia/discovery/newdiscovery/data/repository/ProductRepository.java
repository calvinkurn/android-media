package com.tokopedia.discovery.newdiscovery.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/5/17.
 */

public interface ProductRepository {

    Observable<SearchResultModel> getProduct(TKPDMapParam<String, Object> param);

    void getProductLoadMore();

}
