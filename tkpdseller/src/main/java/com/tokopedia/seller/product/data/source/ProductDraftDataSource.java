package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;

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
}
