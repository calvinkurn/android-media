package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/18/17.
 */

public interface GenerateHostRepository {
    Observable<GenerateHostDomainModel> generateHost();
}
