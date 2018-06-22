package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.HotlistPageTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.helper.IndicatorViewHelper;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.tkpdpdp.customview.RatingView;

import java.util.ArrayList;
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

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_list;

    protected Context context;
    protected ItemClickListener mItemClickListener;

    public ListProductViewHolder(View parent, ItemClickListener mItemClickListener) {
        super(parent);
        context = parent.getContext();
        this.mItemClickListener = mItemClickListener;
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
        renderProductImage(element.getImageUrl());
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
        mItemClickListener.onProductClicked(element, getAdapterPosition());
    }

    protected void onWishlistButtonClick(HotlistProductViewModel element) {
        if (element.isWishlistButtonEnabled()) {
            mItemClickListener.onWishlistClicked(element.getProductID(), element.isWishlist());
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

    protected void renderProductImage(String imageUrl) {
        ImageHandler.loadImageSourceSize(context, productImage, imageUrl);
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
                LuckyShopImage.loadImage(context, badgeItem.getImageUrl(), badgesContainer);
            }
        }
    }
}
