package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.domain.model.ProductListDomain;

import java.util.Map;

import rx.Observable;

/**
 * @author normansyahputa on 2/20/17.
 */

public class TopAdsSearchProductRepositoryImpl implements TopAdsSearchProductRepository {

    private CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource;

    public TopAdsSearchProductRepositoryImpl(
            CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {

        this.cloudTopAdsSearchProductDataSource = cloudTopAdsSearchProductDataSource;
    }

    @Override
    public Observable<ProductListDomain> searchProduct(Map<String, String> param) {
        return cloudTopAdsSearchProductDataSource.searchProduct(param);
    }
}
