package com.tokopedia.seller.product.data.source.cloud;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.data.source.cloud.api.SearchApi;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.CatalogDataModel;
import com.tokopedia.seller.product.constant.ProductNetworkConstant;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */
@ActivityScope
public class CatalogCloud {

    private static final int DEFAULT_START = 1;
    private static final int DEFAULT_ROWS = 20;
    private final SearchApi api;

    @Inject
    public CatalogCloud(SearchApi api) {
        this.api = api;
    }

    // https://phab.tokopedia.com/w/api/ace/search_catalog/
    // https://ace.tokopedia.com/search/v1/catalog?q=samsung&sc=65&device=android&start=0&rows=30&ob=1
    public Observable<CatalogDataModel> fetchData(String keyword,
                                                  int productDepId) {
        return fetchData(keyword, productDepId, DEFAULT_START, DEFAULT_ROWS);
    }

    public Observable<CatalogDataModel> fetchData(String keyword,
                                                  int productDepId,
                                                  int start,
                                                  int rows) {
        Map<String, String> param = new HashMap<>();
        if (productDepId!= 0) {
            param.put(ProductNetworkConstant.PARAM_SC, String.valueOf(productDepId));
        }
        param.put(ProductNetworkConstant.PARAM_Q, keyword);
        param.put(ProductNetworkConstant.PARAM_DEVICE, ProductNetworkConstant.VALUE_SOURCE_ANDROID);

        if (start!= 0) {
            param.put(ProductNetworkConstant.PARAM_START, String.valueOf(start));
        }

        if (rows!= 0) {
            param.put(ProductNetworkConstant.PARAM_ROWS, String.valueOf(rows));
        }

        param.put(ProductNetworkConstant.PARAM_ROWS, ProductNetworkConstant.VALUE_SOURCE_ANDROID);
        return api.getCatalog(param);
    }
}
