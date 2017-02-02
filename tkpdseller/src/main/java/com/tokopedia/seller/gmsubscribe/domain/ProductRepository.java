package com.tokopedia.seller.gmsubscribe.domain;

import com.tokopedia.seller.gmsubscribe.domain.model.product.GMProductDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public interface ProductRepository {
    Observable<List<GMProductDomainModel>> getCurrentProductSelection();

    Observable<List<GMProductDomainModel>> getExtendProductSelection();
}
