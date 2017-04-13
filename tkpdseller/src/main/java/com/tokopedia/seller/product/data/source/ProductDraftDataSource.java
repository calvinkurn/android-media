package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.db.ProductDraftDataManager;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataSource {
    private final ProductDraftDataManager productDraftDataManager;

    public ProductDraftDataSource(ProductDraftDataManager productDraftDataManager) {
        this.productDraftDataManager = productDraftDataManager;
    }

    public Observable<Long> saveDraft(String productDraftDataBase){
        return productDraftDataManager.saveDraft(productDraftDataBase);
    }

    public Observable<String> getDraft(int productId) {
        return productDraftDataManager.getDraft(productId);
    }
}
