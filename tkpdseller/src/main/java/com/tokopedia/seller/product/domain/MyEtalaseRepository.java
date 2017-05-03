package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface MyEtalaseRepository {
    Observable<MyEtalaseDomainModel> fetchMyEtalase();

    Observable<Boolean> addNewEtalase(String newEtalaseName);
}
