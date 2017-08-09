package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.MyEtalaseDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface MyEtalaseRepository {
    Observable<MyEtalaseDomainModel> fetchMyEtalase(int page);

    Observable<Boolean> addNewEtalase(String newEtalaseName);
}
