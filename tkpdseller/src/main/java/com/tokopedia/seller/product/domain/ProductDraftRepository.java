package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductDraftRepository {
    Observable<Long> saveDraft(UploadProductInputDomainModel domainModel);
    Observable<UploadProductInputDomainModel> getDraft(long productId);
}
