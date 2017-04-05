package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.MyEtalaseDomainModel;

import java.util.List;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface MyEtalaseRepository {
    Observable<List<MyEtalaseDomainModel>> fetchMyEtalase();
}
