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
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductViewHolder extends AbstractViewHolder<ShopProductViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_product_grid;
    public static final int SPAN_LOOK_UP = 1;

    private final ShopProductClickedListener shopProductClickedListener;
    private TextView titleTextView;
    private TextView priceTextView;
    private TextView cashBackTextView;
    private TextView wholesaleTextView;
    private TextView preOrderTextView;
    private ImageView freeReturnImageView, productImageView;
    private ImageView wishlistImageView;
    private FrameLayout wishlistContainer;
    private AppCompatRatingBar qualityRatingBar;
    private TextView totalReview;

    public ShopProductViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener) {
        super(itemView);
        this.shopProductClickedListener = shopProductClickedListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        titleTextView = view.findViewById(R.id.title);
        priceTextView = view.findViewById(R.id.price);
        cashBackTextView = view.findViewById(R.id.text_view_cashback);
        wholesaleTextView = view.findViewById(R.id.text_view_wholesale);
        preOrderTextView = view.findViewById(R.id.text_view_pre_order);

        freeReturnImageView = view.findViewById(R.id.image_view_free_return);
        productImageView = view.findViewById(R.id.product_image);
        wishlistImageView = view.findViewById(R.id.image_view_wishlist);
        wishlistContainer = view.findViewById(R.id.wishlist_button_container);

        qualityRatingBar = view.findViewById(R.id.ratingBar);
        totalReview = view.findViewById(R.id.total_review);
    }

    @Override
    public void bind(final ShopProductViewModel shopProductViewModel) {
        updateDisplayGeneralView(shopProductViewModel);
        updateDisplayRating(shopProductViewModel);
        updateDisplayBadges(shopProductViewModel);
        updateDisplayWishList(shopProductViewModel);
    }

    private void updateDisplayGeneralView(final ShopProductViewModel shopProductViewModel) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopProductClickedListener.onProductClicked(shopProductViewModel);
            }
        });

        titleTextView.setText(shopProductViewModel.getName());
        priceTextView.setText(shopProductViewModel.getPrice());
        ImageHandler.LoadImage(productImageView, shopProductViewModel.getImageUrl());
    }

    private void updateDisplayRating(final ShopProductViewModel shopProductViewModel) {
        if (totalReview != null && shopProductViewModel.getTotalReview() > 0) {
            totalReview.setText(String.valueOf(shopProductViewModel.getTotalReview()));
            totalReview.setVisibility(View.VISIBLE);
            if (qualityRatingBar != null) {
                qualityRatingBar.setRating((float) shopProductViewModel.getRating());
                qualityRatingBar.setMax(ShopPageActivity.MAX_RATING_STAR);
                qualityRatingBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateDisplayBadges(final ShopProductViewModel shopProductViewModel) {
        if (shopProductViewModel.getCashback() > 0) {
            cashBackTextView.setText(cashBackTextView.getContext().getString(
                    R.string.product_manage_item_cashback, (int) shopProductViewModel.getCashback()));
            cashBackTextView.setVisibility(View.VISIBLE);
        } else {
            cashBackTextView.setVisibility(View.GONE);
        }
        freeReturnImageView.setVisibility(shopProductViewModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        preOrderTextView.setVisibility(shopProductViewModel.isPo() ? View.VISIBLE : View.GONE);
        wholesaleTextView.setVisibility(shopProductViewModel.isWholesale() ? View.VISIBLE : View.GONE);
    }

    private void updateDisplayWishList(final ShopProductViewModel shopProductViewModel) {
        wishlistContainer.setVisibility(shopProductViewModel.isShowWishList() ? View.VISIBLE : View.GONE);
        wishlistImageView.setImageResource(shopProductViewModel.isWishList() ? R.drawable.ic_wishlist_checked : R.drawable.ic_wishlist_unchecked);
        wishlistContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopProductClickedListener.onWishListClicked(shopProductViewModel);
            }
        });
    }
}