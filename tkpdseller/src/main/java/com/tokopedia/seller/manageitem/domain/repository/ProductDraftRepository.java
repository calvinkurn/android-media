package com.tokopedia.seller.manageitem.domain.repository;


import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;
import com.tokopedia.seller.manageitem.data.db.ProductDraftViewModel;

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

    Observable<List<ProductDraftViewModel>> getAllDraft();

    Observable<Long> getAllDraftCount();

    Observable<Long> updateDraftToUpload(long draftProductIdToUpdate, ProductViewModel domainModel, boolean isUploading);

    Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading);
}
