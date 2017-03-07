package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.SearchProductEOFMapper;
import com.tokopedia.seller.topads.data.source.TopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.domain.model.ProductListDomain;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/17/17.
 */

public class CloudTopAdsSearchProductDataSource implements TopAdsSearchProductDataSource {

    private Context context;
    private TopAdsManagementService topAdsSearchProductService;
    private SearchProductEOFMapper searchProductMapper;

    public CloudTopAdsSearchProductDataSource(Context context,
                                              TopAdsManagementService topAdsSearchProductService,
                                              SearchProductEOFMapper searchProductMapper) {
        this.context = context;
        this.topAdsSearchProductService = topAdsSearchProductService;
        this.searchProductMapper = searchProductMapper;
    }

    @Override
    public Observable<ProductListDomain> searchProduct(Map<String, String> param) {
        return topAdsSearchProductService
                .getApi()
                .searchProductAd(param)
                .map(searchProductMapper);
    }
}
