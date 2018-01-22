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

    ShopReviewHolderListener shopReviewHolderListener;

    private TextView productName;
    private ImageView productImage;

    public ShopReviewViewHolder(View itemView, ListenerReviewHolder viewListener, ShopReviewHolderListener shopReviewHolderListener) {
        super(itemView, viewListener);
        productName = itemView.findViewById(R.id.product_name);
        productImage = itemView.findViewById(R.id.product_image);
        this.shopReviewHolderListener = shopReviewHolderListener;
    }

    @Override
    public void bind(ProductReviewModelContent element) {
        super.bind(element);
        if(element instanceof ShopReviewModelContent){
            final ShopReviewModelContent shopReviewModelContent = (ShopReviewModelContent) element;
            productName.setText(shopReviewModelContent.getProductName());
            ImageHandler.loadImageRounded2(itemView.getContext(), productImage, shopReviewModelContent.getProductImageUrl());
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shopReviewHolderListener.onGoToDetailProduct(shopReviewModelContent.getProductPageUrl());
                }
            });
        }
    }

    public interface ShopReviewHolderListener{
        void onGoToDetailProduct(String productUrl);
    }
}
