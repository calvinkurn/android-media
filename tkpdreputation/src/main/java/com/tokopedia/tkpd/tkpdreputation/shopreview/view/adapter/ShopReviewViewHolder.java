package com.tokopedia.tkpd.tkpdreputation.shopreview.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewContentViewHolder;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelContent;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewViewHolder extends ProductReviewContentViewHolder {
    public static final int LAYOUT = R.layout.item_shop_review;

    private TextView productName;
    private ImageView productImage;

    public ShopReviewViewHolder(View itemView, ListenerReviewHolder viewListener) {
        super(itemView, viewListener);
        productName = itemView.findViewById(R.id.product_name);
        productImage = itemView.findViewById(R.id.product_image);
    }

    @Override
    public void bind(ProductReviewModelContent element) {
        super.bind(element);
        if(element instanceof ShopReviewModelContent){
            ShopReviewModelContent shopReviewModelContent = (ShopReviewModelContent) element;
            productName.setText(shopReviewModelContent.getProductName());
            ImageHandler.loadImageRounded2(itemView.getContext(), productImage, shopReviewModelContent.getProductImageUrl());
        }
    }
}
