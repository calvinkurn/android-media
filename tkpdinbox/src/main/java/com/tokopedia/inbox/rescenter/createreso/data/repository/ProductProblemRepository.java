package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public interface ProductProblemRepository {
    Observable<ProductProblemResponseDomain> getProductProblemFromCloud(RequestParams requestParams);
}
