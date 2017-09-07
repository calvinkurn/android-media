package com.tokopedia.seller.product.draft.data.source;

import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.draft.data.source.db.ProductDraftDataManager;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataSource {
    private final ProductDraftDataManager productDraftDataManager;

    @Inject
    public ProductDraftDataSource(ProductDraftDataManager productDraftDataManager) {
        this.productDraftDataManager = productDraftDataManager;
    }

    public Observable<Long> saveDraft(String productDraftDataBase, long draftId, boolean isUploading, String userId){
        return productDraftDataManager.saveDraft(productDraftDataBase, draftId, isUploading, userId);
    }

    public Observable<String> getDraft(long productId) {
        return productDraftDataManager.getDraft(productId);
    }

    public Observable<List<ProductDraftDataBase>> getAllDraft(String userId) {
        return productDraftDataManager.getAllDraft(userId);
    }

    public Observable<Long> getAllDraftCount(String userId) {
        return productDraftDataManager.getAllDraftCount(userId);
    }

    public Observable<Boolean> clearAllDraft(String userId) {
        return productDraftDataManager.clearAllDraft(userId);
    }

    public Observable<Boolean> deleteDraft(long productId) {
        return productDraftDataManager.deleteDeraft(productId);
    }

    public Observable<Long> updateDraft(long productId, String draftDataBase) {
        return productDraftDataManager.updateDraft(productId, draftDataBase);
    }

    public Observable<Long> updateDraft(long productId, String draftDataBase, boolean isUploading) {
        return productDraftDataManager.updateDraft(productId, draftDataBase, isUploading);
    }

    public Observable<Boolean> updateUploadingStatusDraft(long productId, boolean draftDataBase) {
        return productDraftDataManager.updateUploadingStatusDraft(productId, draftDataBase);
    }

    public Observable<Boolean> updateBlankUserId(String userId) {
        return productDraftDataManager.updateBlankUserId(userId);
    }
}
