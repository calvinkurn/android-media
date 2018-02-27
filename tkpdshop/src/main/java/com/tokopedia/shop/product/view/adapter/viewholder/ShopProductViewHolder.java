package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.shop.R;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductViewHolder extends AbstractViewHolder<ShopProductViewModel> {

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
    private ImageView wishlistImageView;
    private FrameLayout wishlistContainer;
    private int imageGuideLineGrid;
    private int imageGuideLineList;

    private ShopProductVHListener shopProductVHListener;
    private ShopProductViewModel model;
    private AppCompatRatingBar qualityRatingBar;
    private TextView totalReview;

    public ShopProductViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    protected ShopProductVHListener getShopProductVHListener() {
        return shopProductVHListener;
    }

    public ShopProductViewHolder setShopProductVHListener(ShopProductVHListener shopProductVHListener) {
        this.shopProductVHListener = shopProductVHListener;
        return this;
    }

    public ShopProductViewModel getModel() {
        return model;
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
        wishlistImageView = view.findViewById(R.id.image_view_wishlist);
        wishlistContainer = view.findViewById(R.id.wishlist_button_container);

        qualityRatingBar = view.findViewById(R.id.ratingBar);
        totalReview = view.findViewById(R.id.total_review);
    }

    @Override
    public void bind(ShopProductViewModel model) {
        this.model = model;

        titleTextView.setText(getModel().getName());
        priceTextView.setText(getModel().getPrice());
        ImageHandler.LoadImage(productImageView, getModel().getImageUrl());
        if (model.isWishList()) {
            wishlistImageView.setImageResource(R.drawable.ic_wishlist_red);
        } else {
            wishlistImageView.setImageResource(R.drawable.ic_wishlist);
        }

        wishlistContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getShopProductVHListener()!= null){
                    getShopProductVHListener().onWishlist(
                            ShopProductViewHolder.this.model
                    );
                }
            }
        });

        if (qualityRatingBar != null) {
            qualityRatingBar.setRating((float) model.getRating());
            qualityRatingBar.setMax(ShopPageActivity.MAX_RATING_STAR);
        }

        if (totalReview != null) {
            totalReview.setText(Integer.toString(model.getTotalReview()));
        }

        if (model.getCashback() > 0) {
            cashbackTextView.setText(cashbackTextView.getContext().getString(
                    R.string.product_manage_item_cashback, model.getCashback()));
            cashbackTextView.setVisibility(View.VISIBLE);
        } else {
            cashbackTextView.setVisibility(View.GONE);
        }

        freeReturnImageView.setVisibility(model.isFreeReturn() ? View.VISIBLE : View.GONE);

        preOrderTextView.setVisibility(model.isPo() ? View.VISIBLE : View.GONE);

        wholesaleTextView.setVisibility(model.isWholesale() ? View.VISIBLE : View.GONE);
    }

    public interface ShopProductVHListener{
        void onWishlist(ShopProductViewModel model);
    }
}