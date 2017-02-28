package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.domain.model.ProductDomain;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by normansyahputa on 2/20/17.
 */

public class TopAdsSearchProductRepositoryImpl implements TopAdsSearchProductRepository {

    private CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource;

    public TopAdsSearchProductRepositoryImpl(
            CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {

        this.cloudTopAdsSearchProductDataSource = cloudTopAdsSearchProductDataSource;
    }

    @Override
    public Observable<List<ProductDomain>> searchProduct(Map<String, String> param) {
        return cloudTopAdsSearchProductDataSource.searchProduct(param);
    }
}
