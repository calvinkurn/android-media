package com.tokopedia.seller.gmsubscribe.data.repository;

import com.tokopedia.seller.gmsubscribe.data.factory.GMSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.source.product.GMSubscribeProductSource;
import com.tokopedia.seller.gmsubscribe.domain.product.GMSubscribeProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */

public class GMSubscribeProductRepositoryImpl implements GMSubscribeProductRepository {
    private final GMSubscribeProductFactory gmSubscribeProductFactory;

    public GMSubscribeProductRepositoryImpl(GMSubscribeProductFactory gmSubscribeProductFactory) {
        this.gmSubscribeProductFactory = gmSubscribeProductFactory;
    }

    @Override
    public Observable<List<GMProductDomainModel>> getCurrentProductSelection() {
        GMSubscribeProductSource gmSubscribeProductSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSource.getCurrentProductSelection();
    }

    @Override
    public Observable<List<GMProductDomainModel>> getExtendProductSelection() {
        GMSubscribeProductSource gmSubscribeProductSource = gmSubscribeProductFactory.createGMSubscribeProductSource();
        return gmSubscribeProductSource.getExtendProductSelection();
    }
}
