package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.SearchProductMapper;
import com.tokopedia.seller.topads.data.source.TopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.domain.model.ProductDomain;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/17/17.
 */

public class CloudTopAdsSearchProductDataSource implements TopAdsSearchProductDataSource {

    private Context context;
    private TopAdsManagementService topAdsSearchProductService;
    private SearchProductMapper searchProductMapper;

    public CloudTopAdsSearchProductDataSource(Context context,
                                              TopAdsManagementService topAdsSearchProductService,
                                              SearchProductMapper searchProductMapper) {
        this.context = context;
        this.topAdsSearchProductService = topAdsSearchProductService;
        this.searchProductMapper = searchProductMapper;
    }

    @Override
    public Observable<List<ProductDomain>> searchProduct(Map<String, String> param) {
        return topAdsSearchProductService
                .getApi()
                .searchProductAd(param)
                .map(searchProductMapper);
    }
}
