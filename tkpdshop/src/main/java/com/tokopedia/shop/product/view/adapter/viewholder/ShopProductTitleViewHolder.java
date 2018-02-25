package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductTitleViewHolder extends AbstractViewHolder<ShopProductFeaturedViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_product_grid;

    public static final int SPAN_LOOK_UP = 1;

    private TextView titleTextView;
    private TextView originalPriceTextView;
    private TextView priceTextView;
    private TextView discountTextView;
    private TextView variantTextView;
    private TextView cashbackTextView;
    private TextView wholesaleTextView;
    private TextView preOrderTextView;

    private ImageView featuredProductImageView, freeReturnImageView, productImageView;
    private FrameLayout wishlistContainer;

    private ViewHolderListener viewHolderListener;
    private Guideline guideLine;
    private int imageGuideLineGrid;
    private int imageGuideLineList;


    public ShopProductTitleViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    public ShopProductTitleViewHolder setViewHolderListener(ViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
        return this;
    }

    private void findViews(View view) {
        titleTextView = view.findViewById(R.id.title);
        originalPriceTextView = view.findViewById(R.id.text_original_price);
        priceTextView = view.findViewById(R.id.price);
        variantTextView = view.findViewById(R.id.text_view_variant);
        cashbackTextView = view.findViewById(R.id.text_view_cashback);
        wholesaleTextView = view.findViewById(R.id.text_view_wholesale);
        preOrderTextView = view.findViewById(R.id.text_view_pre_order);
        discountTextView = view.findViewById(R.id.text_discount);

        featuredProductImageView = view.findViewById(R.id.image_view_featured);
        freeReturnImageView = view.findViewById(R.id.image_view_free_return);
        productImageView = view.findViewById(R.id.product_image);

        wishlistContainer = view.findViewById(R.id.wishlist_button_container);

        guideLine = view.findViewById(R.id.guideline3);

    }

    @Override
    public void bind(ShopProductFeaturedViewModel element) {
//        titleTextView.setText(element.getProductName());
//        priceTextView.setText(element.getProductPrice());
//        ImageHandler.LoadImage(productImageView, element.getProductImage700());
    }

    public interface ViewHolderListener{
        int getLayoutManagerType();
    }
}