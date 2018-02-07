package com.tokopedia.seller.product.draft.domain.model;

import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductDraftRepository {
    Observable<Long> saveDraft(ProductViewModel domainModel, boolean isUploading);
    Observable<ProductViewModel> getDraft(long productId);

    Observable<Boolean> clearAllDraft();

    Observable<Boolean> deleteDraft(long productId);

    Observable<List<ProductViewModel>> getAllDraft();

    Observable<Long> getAllDraftCount();

    Observable<Long> updateDraftToUpload(long productId, ProductViewModel domainModel, boolean isUploading);

    Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading);
}
