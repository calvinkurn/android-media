package com.tokopedia.seller.gmsubscribe.domain.product;

import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public interface GMSubscribeProductRepository {
    Observable<List<GMProductDomainModel>> getCurrentProductSelection();

    Observable<List<GMProductDomainModel>> getExtendProductSelection();
}
