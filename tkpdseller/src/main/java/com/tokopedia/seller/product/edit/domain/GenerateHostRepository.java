package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/18/17.
 */

public interface GenerateHostRepository {
    Observable<GenerateHostDomainModel> generateHost();
}
