package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductDraftRepository {
    Observable<Long> saveDraft(UploadProductInputDomainModel domainModel, boolean isUploading);
    Observable<UploadProductInputDomainModel> getDraft(long productId);

    Observable<Boolean> clearAllDraft();

    Observable<Boolean> deleteDraft(long productId);

    Observable<List<UploadProductInputDomainModel>> getAllDraft();

    Observable<Long> getAllDraftCount();

    Observable<Boolean> updateDraft(long productId, UploadProductInputDomainModel domainModel);

    Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading);

}
