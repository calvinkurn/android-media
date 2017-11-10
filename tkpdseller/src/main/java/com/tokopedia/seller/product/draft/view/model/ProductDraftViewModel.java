package com.tokopedia.seller.product.draft.view.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by User on 6/21/2017.
 */

public class ProductDraftViewModel implements ItemType {
    public static final int TYPE = 1;

    private long productId;
    private String primaryImageUrl;
    private String productName;
    private int completionPercent;
    private boolean isEdit;

    public ProductDraftViewModel(long productId, String primaryImageUrl,
                                 String productName, int completionPercent, boolean isEdit) {
        this.productId = productId;
        this.primaryImageUrl = primaryImageUrl;
        this.productName = productName;
        this.completionPercent = completionPercent;
        this.isEdit = isEdit;
    }

    public long getProductId() {
        return productId;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public int getCompletionPercent() {
        return completionPercent;
    }

    public boolean isEdit() {
        return isEdit;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
