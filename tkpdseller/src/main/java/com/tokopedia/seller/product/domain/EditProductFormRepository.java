package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.EditProductFormDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface EditProductFormRepository {
    Observable<EditProductFormDomainModel> fetchEditProduct(String productId);
}
