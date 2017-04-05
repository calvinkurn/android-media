package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.MyEtalaseListDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface MyEtalaseRepository {
    Observable<MyEtalaseListDomainModel> fetchMyEtalase();
}
