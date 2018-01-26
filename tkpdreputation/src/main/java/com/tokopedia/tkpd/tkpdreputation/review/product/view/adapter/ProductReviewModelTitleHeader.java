package com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ProductReviewModelTitleHeader implements ProductReviewModel {
    private String title;

    public ProductReviewModelTitleHeader(String title) {
        this.title = title;
    }

    @Override
    public int type(ProductReviewTypeFactoryAdapter typeFactory) {
        return typeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }
}
