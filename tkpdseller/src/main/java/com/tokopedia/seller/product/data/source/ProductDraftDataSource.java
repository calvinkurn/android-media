package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;

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

    public Observable<Long> saveDraft(String productDraftDataBase, long draftId){
        return productDraftDataManager.saveDraft(productDraftDataBase, draftId);
    }

    public Observable<String> getDraft(long productId) {
        return productDraftDataManager.getDraft(productId);
    }

    public Observable<List<ProductDraftDataBase>> getAllDraft() {
        return productDraftDataManager.getAllDraft();
    }

    public Observable<Long> getAllDraftCount() {
        return productDraftDataManager.getAllDraftCount();
    }

    public Observable<Boolean> clearAllDraft() {
        return productDraftDataManager.clearAllDraft();
    }

    public void deleteDraft(long productId) {
        productDraftDataManager.deleteDeraft(productId);
    }

    public void updateDraft(long productId, String draftDataBase) {
        productDraftDataManager.updateDraft(productId, draftDataBase);
    }
}
