package com.tokopedia.seller.manageitem.domain.repository;


import com.tokopedia.seller.manageitem.data.model.GenerateHostDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/18/17.
 */

public interface GenerateHostRepository {
    Observable<GenerateHostDomainModel> generateHost();
}
