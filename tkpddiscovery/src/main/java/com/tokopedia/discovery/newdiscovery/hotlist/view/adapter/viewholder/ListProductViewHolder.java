package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
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
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
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
    protected TextView shopName;
    protected TextView location;
    protected TextView reviewCount;
    protected ImageView productImage;
    protected ImageView rating;
    protected ImageView wishlistButton;
    protected FlowLayout labelContainer;
    protected RelativeLayout wishlistButtonContainer;
    protected LinearLayout badgesContainer;
    protected LinearLayout ratingReviewContainer;
    protected View container;

    @LayoutRes
    public static final int LAYOUT = R.layout.listview_hotlist_product;

    private final Context context;
    private final ItemClickListener mItemClickListener;

    public ListProductViewHolder(View parent, ItemClickListener mItemClickListener) {
        super(parent);
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
        container = parent.findViewById(R.id.container);
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
        ImageHandler.loadImageThumbs(context, productImage, imageUrl);
    }

    protected void renderShopLocation(String shopCity) {
        if (shopCity != null && !shopCity.isEmpty()) {
            location.setVisibility(View.VISIBLE);
            location.setText(MethodChecker.fromHtml(shopCity));
        } else {
            location.setVisibility(View.INVISIBLE);
        }
    }

    protected void renderLabels(List<HotlistProductViewModel.LabelModel> labelList) {
        List<String> titles = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < labelList.size(); i++) {
            titles.add(labelList.get(i).getTitle());
            colors.add(labelList.get(i).getColor());
        }
        IndicatorViewHelper.renderLabelsViewV2(context, labelContainer, titles, colors);
    }

    protected void renderBadges(List<HotlistProductViewModel.BadgeModel> badgesList) {
        List<String> badgesImageUrl = new ArrayList<>();
        for (HotlistProductViewModel.BadgeModel model : badgesList) {
            badgesImageUrl.add(model.getImageUrl());
        }
        IndicatorViewHelper.renderBadgesViewV2(context, badgesContainer, badgesImageUrl);
    }

}
