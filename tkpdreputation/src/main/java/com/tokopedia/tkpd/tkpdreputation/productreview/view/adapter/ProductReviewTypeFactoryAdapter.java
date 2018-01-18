package com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.presenter.ProductReviewContract;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ProductReviewTypeFactoryAdapter extends BaseAdapterTypeFactory {
    private final ProductReviewContract.View viewListener;

    public ProductReviewTypeFactoryAdapter(ProductReviewContract.View viewListener) {
        this.viewListener = viewListener;
    }

    public int type(ProductReviewModelTitleHeader productReviewModelTitleHeader) {
        return ProductReviewTitleHeaderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ProductReviewTitleHeaderViewHolder.LAYOUT){
            return new ProductReviewTitleHeaderViewHolder(parent);
        }else if(type == ProductReviewContentViewHolder.LAYOUT) {
            return new ProductReviewContentViewHolder(parent, viewListener);
        }else{
            return super.createViewHolder(parent, type);
        }
    }

    public int type(ProductReviewModelContent productReviewModelContent) {
        return ProductReviewContentViewHolder.LAYOUT;
    }
}
