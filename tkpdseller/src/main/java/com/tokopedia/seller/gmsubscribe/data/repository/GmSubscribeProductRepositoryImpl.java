package com.tokopedia.seller.gmsubscribe.data.repository;

import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.source.product.GmSubscribeProductSelectorDataSource;
import com.tokopedia.seller.gmsubscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmAutoSubscribeDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */

public class GmSubscribeProductRepositoryImpl implements GmSubscribeProductRepository {
    private final GmSubscribeProductFactory gmSubscribeProductFactory;

    public GmSubscribeProductRepositoryImpl(GmSubscribeProductFactory gmSubscribeProductFactory) {
        this.gmSubscribeProductFactory = gmSubscribeProductFactory;
    }

    @Override
    public Observable<List<GmProductDomainModel>> getCurrentProductSelection() {
        GmSubscribeProductSelectorDataSource gmSubscribeProductSelectorDataSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSelectorDataSource.getCurrentProductSelection();
    }

    @Override
    public Observable<List<GmProductDomainModel>> getExtendProductSelection() {
        GmSubscribeProductSelectorDataSource gmSubscribeProductSelectorDataSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSelectorDataSource.getExtendProductSelection();
    }

    @Override
    public Observable<GmProductDomainModel> getCurrentProductSelectedData(Integer productId) {
        GmSubscribeProductSelectorDataSource gmSubscribeProductSelectorDataSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSelectorDataSource.getCurrentProductSelectedData(productId);
    }

    @Override
    public Observable<GmAutoSubscribeDomainModel> getExtendProductSelectedData(Integer autoSubscribeProductId, Integer productId) {
        GmSubscribeProductSelectorDataSource gmSubscribeProductSelectorDataSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSelectorDataSource.getExtendProductSelectedData(autoSubscribeProductId, productId);
    }

    @Override
    public Observable<Boolean> clearGMProductCache() {
        GmSubscribeProductSelectorDataSource gmSubscribeProductSelectorDataSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSelectorDataSource.clearGMSubscribeProductCache();
    }

}
