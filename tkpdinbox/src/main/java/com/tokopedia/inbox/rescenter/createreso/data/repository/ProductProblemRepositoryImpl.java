package com.tokopedia.inbox.rescenter.createreso.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.createreso.data.factory.ProductProblemFactory;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;

import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProductProblemRepositoryImpl implements ProductProblemRepository {

    ProductProblemFactory productProblemFactory;

    public ProductProblemRepositoryImpl(ProductProblemFactory productProblemFactory) {
        this.productProblemFactory = productProblemFactory;
    }

    @Override
    public Observable<ProductProblemResponseDomain> getProductProblemFromCloud(RequestParams requestParams) {
        return productProblemFactory.getProductProblemCloudSource().getProductProblemList(requestParams);
    }
}
