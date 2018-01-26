package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ProductReviewContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ProductReviewTypeFactoryAdapter;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewTypeFactoryAdapter extends ProductReviewTypeFactoryAdapter {

    private ShopReviewViewHolder.ShopReviewHolderListener shopReviewHolderListener;

    public ShopReviewTypeFactoryAdapter(ProductReviewContentViewHolder.ListenerReviewHolder viewListener,
                                        ShopReviewViewHolder.ShopReviewHolderListener shopReviewHolderListener) {
        super(viewListener);
        this.shopReviewHolderListener = shopReviewHolderListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ShopReviewViewHolder.LAYOUT){
            return new ShopReviewViewHolder(parent, viewListener, shopReviewHolderListener);
        }else{
            return super.createViewHolder(parent, type);
        }
    }

    public int type(ShopReviewModelContent shopReviewModelContent) {
        return ShopReviewViewHolder.LAYOUT;
    }
}
