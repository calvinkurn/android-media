package com.tokopedia.tkpd.tkpdreputation.shopreview.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewTypeFactoryAdapter;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewTypeFactoryAdapter extends ProductReviewTypeFactoryAdapter {

    public ShopReviewTypeFactoryAdapter(ProductReviewContentViewHolder.ListenerReviewHolder viewListener) {
        super(viewListener);
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ShopReviewViewHolder.LAYOUT){
            return new ShopReviewViewHolder(parent, viewListener);
        }else{
            return super.createViewHolder(parent, type);
        }
    }

    public int type(ShopReviewModelContent shopReviewModelContent) {
        return ShopReviewViewHolder.LAYOUT;
    }
}
