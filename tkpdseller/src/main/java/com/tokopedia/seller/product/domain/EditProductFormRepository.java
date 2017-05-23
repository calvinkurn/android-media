package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface EditProductFormRepository {
    Observable<UploadProductInputDomainModel> fetchEditProduct(String productId);
}
