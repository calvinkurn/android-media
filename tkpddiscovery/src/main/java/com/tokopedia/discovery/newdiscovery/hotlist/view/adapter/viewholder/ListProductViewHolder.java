package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.view.RatingView;

import java.util.List;

/**
 * Created by hangnadi on 10/9/17.
 */

@SuppressWarnings("WeakerAccess")
public class ListProductViewHolder extends AbstractViewHolder<HotlistProductViewModel> {

    protected TextView title;
    protected TextView price;
    protected TextView location;
    protected TextView reviewCount;
    protected ImageView productImage;
    protected ImageView rating;
    protected ImageView wishlistButton;
    protected RelativeLayout wishlistButtonContainer;
    protected LinearLayout badgesContainer;
    protected LinearLayout ratingReviewContainer;
    protected View container;
    protected TextView topLabel;
    protected TextView bottomLabel;
    protected SimpleTarget<Bitmap> simpleTargetBitmapShopBadge;

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_list;

    protected Context context;
    protected HotlistListener mHotlistListener;

    public ListProductViewHolder(View parent, HotlistListener mHotlistListener) {
        super(parent);
        context = parent.getContext();
        this.mHotlistListener = mHotlistListener;
        title = (TextView) parent.findViewById(R.id.title);
        price = (TextView) parent.findViewById(R.id.price);
        location = (TextView) parent.findViewById(R.id.location);
        reviewCount = (TextView) parent.findViewById(R.id.review_count);
        productImage = (ImageView) parent.findViewById(R.id.product_image);
        rating = (ImageView) parent.findViewById(R.id.rating);
        wishlistButton = (ImageView) parent.findViewById(R.id.wishlist_button);
        wishlistButtonContainer = (RelativeLayout) parent.findViewById(R.id.wishlist_button_container);
        badgesContainer = (LinearLayout) parent.findViewById(R.id.badges_container);
        ratingReviewContainer = (LinearLayout) parent.findViewById(R.id.rating_review_container);
        container = parent.findViewById(R.id.container);
        topLabel = itemView.findViewById(R.id.topLabel);
        bottomLabel = itemView.findViewById(R.id.bottomLabel);
    }

    @Override
    public void bind(final HotlistProductViewModel element) {
        if (!TextUtils.isEmpty(element.getTopLabel())) {
            topLabel.setText(element.getTopLabel());
            topLabel.setVisibility(View.VISIBLE);
        } else {
            topLabel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(element.getBottomLabel())) {
            bottomLabel.setText(element.getBottomLabel());
            bottomLabel.setVisibility(View.VISIBLE);
        } else {
            bottomLabel.setVisibility(View.GONE);
        }
        title.setText(MethodChecker.fromHtml(element.getProductName()));

        String priceText = !TextUtils.isEmpty(element.getPriceRange()) ?
                element.getPriceRange() : element.getPrice();
        price.setText(priceText);

        renderWishlistButton(element.isWishlist());
        renderShopLocation(element);
        renderProductImage(element);
        renderBadges(element.getBadgesList());
        renderRating(element.getRating(), element.getCountReview());

        wishlistButtonContainer.setVisibility(View.VISIBLE);
        wishlistButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWishlistButtonClick(element);
            }
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClicked(element);
            }
        });
    }

    protected void onProductClicked(HotlistProductViewModel element) {
        mHotlistListener.onProductClicked(element, getAdapterPosition());
    }

    protected void onWishlistButtonClick(HotlistProductViewModel element) {
        if (element.isWishlistButtonEnabled()) {
            mHotlistListener.onWishlistClicked(getAdapterPosition(),
                    element.getProductName(), element.getProductID(), element.isWishlist());
        }
    }

    protected void renderRating(String ratingValue, String countReview) {
        if (ratingValue.isEmpty() || countReview.equals("0")) {
            ratingReviewContainer.setVisibility(View.GONE);
        } else {
            ratingReviewContainer.setVisibility(View.VISIBLE);
            rating.setImageResource(
                    RatingView.getRatingDrawable(Integer.parseInt(ratingValue))
            );
            reviewCount.setText(String.format("(%s)", countReview));
        }
    }

    protected void renderWishlistButton(boolean wishlist) {
        if (wishlist) {
            wishlistButton.setBackgroundResource(R.drawable.ic_wishlist_red);
        } else {
            wishlistButton.setBackgroundResource(R.drawable.ic_wishlist);
        }
    }

    protected void renderProductImage(HotlistProductViewModel productItem) {
        ImageHandler.loadImageThumbs(context.getApplicationContext(), productImage, productItem.getImageUrl());
    }

    protected void renderShopLocation(HotlistProductViewModel element) {
        if (!TextUtils.isEmpty(element.getShopCity())) {
            if (isBadgesExist(element)) {
                location.setText(" \u2022 " + MethodChecker.fromHtml(element.getShopCity()));
            } else {
                location.setText(MethodChecker.fromHtml(element.getShopCity()));
            }
            location.setVisibility(View.VISIBLE);
        } else {
            location.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isBadgesExist(HotlistProductViewModel element) {
        List<HotlistProductViewModel.BadgeModel> badgesList = element.getBadgesList();
        if (badgesList == null || badgesList.isEmpty()) {
            return false;
        }

        for (HotlistProductViewModel.BadgeModel badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                return true;
            }
        }
        return false;
    }

    protected void renderBadges(List<HotlistProductViewModel.BadgeModel> badgesList) {
        badgesContainer.removeAllViews();

        for (HotlistProductViewModel.BadgeModel badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                loadShopBadgeIcon(badgeItem);
            }
        }
    }

    private void loadShopBadgeIcon(HotlistProductViewModel.BadgeModel badgeItem) {
        if(simpleTargetBitmapShopBadge == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
            simpleTargetBitmapShopBadge = createSimpleTargetBitmapForLoadBadge(view);
        }

        ImageHandler.loadImageBitmap2(context.getApplicationContext(), badgeItem.getImageUrl(), simpleTargetBitmapShopBadge);
    }

    private SimpleTarget<Bitmap> createSimpleTargetBitmapForLoadBadge(final View view) {
        return new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                loadShopBadgeSuccess(bitmap, view);
            }

            @Override
            public void onLoadFailed(@Nullable Exception e, @Nullable Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                loadShopBadgeFailed(view);
            }
        };
    }

    private void loadShopBadgeSuccess(Bitmap bitmap, View view) {
        ImageView image = view.findViewById(R.id.badge);

        if(image != null) {
            if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                view.setVisibility(View.GONE);
            } else {
                image.setImageBitmap(bitmap);
                view.setVisibility(View.VISIBLE);
                badgesContainer.addView(view);
            }
        }
    }

    private void loadShopBadgeFailed(View view) {
        view.setVisibility(View.GONE);
    }
}
