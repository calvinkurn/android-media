package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;

import java.util.List;

/**
 * Created by hangnadi on 10/8/17.
 */

public class GridProductViewHolder extends ListProductViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.gridview_hotlist_product;

    private final Context context;
    private final ItemClickListener mItemClickListener;

    public GridProductViewHolder(View parent, ItemClickListener mItemClickListener) {
        super(parent, mItemClickListener);
        context = parent.getContext();
        this.mItemClickListener = mItemClickListener;
        title = (TextView) parent.findViewById(R.id.title);
        price = (TextView) parent.findViewById(R.id.price);
        shopName = (TextView) parent.findViewById(R.id.shop_name);
        location = (TextView) parent.findViewById(R.id.location);
        reviewCount = (TextView) parent.findViewById(R.id.review_count);
        productImage = (ImageView) parent.findViewById(R.id.product_image);
        rating = (ImageView) parent.findViewById(R.id.rating);
        wishlistButton = (ImageView) parent.findViewById(R.id.wishlist_button);
        labelContainer = (FlowLayout) parent.findViewById(R.id.label_container);
        wishlistButtonContainer = (RelativeLayout) parent.findViewById(R.id.wishlist_button_container);
        badgesContainer = (LinearLayout) parent.findViewById(R.id.badges_container);
        ratingReviewContainer = (LinearLayout) parent.findViewById(R.id.rating_review_container);
        container = parent;
    }

    @Override
    public void bind(final HotlistProductViewModel element) {
        title.setText(MethodChecker.fromHtml(element.getProductName()));
        shopName.setText(MethodChecker.fromHtml(element.getShopName()));

        price.setText(element.getPrice());

        renderWishlistButton(element.isWishlist());
        renderShopLocation(element.getShopCity());
        renderProductImage(element.getImageUrl());
        renderBadges(element.getBadgesList());
        renderLabels(element.getLabelList());
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

    @Override
    protected void renderRating(String ratingValue, String countReview) {
        super.renderRating(ratingValue, countReview);
    }

    @Override
    protected void renderWishlistButton(boolean wishlist) {
        super.renderWishlistButton(wishlist);
    }

    @Override
    protected void renderProductImage(String imageUrl) {
        super.renderProductImage(imageUrl);
    }

    @Override
    protected void renderShopLocation(String shopCity) {
        super.renderShopLocation(shopCity);
    }

    @Override
    protected void renderLabels(List<HotlistProductViewModel.LabelModel> labelList) {
        super.renderLabels(labelList);
    }

    @Override
    protected void renderBadges(List<HotlistProductViewModel.BadgeModel> badgesList) {
        super.renderBadges(badgesList);
    }
}
