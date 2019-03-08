package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.tkpdpdp.customview.RatingView;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.domain.model.ProductImage;
import com.tokopedia.topads.sdk.utils.ImpresionTask;
import com.tokopedia.topads.sdk.view.ImpressedImageView;

import java.util.List;

/**
 * Created by henrypriyono on 10/11/17.
 */

public class GridProductItemViewHolder extends AbstractViewHolder<ProductItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_grid;

    protected ImpressedImageView productImage;
    private TextView title;
    private TextView price;
    private TextView location;
    private LinearLayout badgesContainer;
    private ImageView wishlistButton;
    private RelativeLayout wishlistButtonContainer;
    private View container;
    private ImageView rating;
    private TextView reviewCount;
    private LinearLayout ratingReviewContainer;
    private ProductListener itemClickListener;
    protected Context context;
    private TextView topLabel;
    private TextView bottomLabel;
    private TextView newLabel;

    public GridProductItemViewHolder(View itemView, ProductListener itemClickListener) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        title = (TextView) itemView.findViewById(R.id.title);
        price = (TextView) itemView.findViewById(R.id.price);
        location = (TextView) itemView.findViewById(R.id.location);
        badgesContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        wishlistButton = (ImageView) itemView.findViewById(R.id.wishlist_button);
        wishlistButtonContainer = (RelativeLayout) itemView.findViewById(R.id.wishlist_button_container);
        container = itemView.findViewById(R.id.container);
        rating = (ImageView) itemView.findViewById(R.id.rating);
        reviewCount = (TextView) itemView.findViewById(R.id.review_count);
        ratingReviewContainer = (LinearLayout) itemView.findViewById(R.id.rating_review_container);
        topLabel = itemView.findViewById(R.id.topLabel);
        bottomLabel = itemView.findViewById(R.id.bottomLabel);
        newLabel = itemView.findViewById(R.id.new_label);
        context = itemView.getContext();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(final ProductItem productItem) {
        if (!TextUtils.isEmpty(productItem.getTopLabel())) {
            topLabel.setText(productItem.getTopLabel());
            topLabel.setVisibility(View.VISIBLE);
        } else {
            topLabel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(productItem.getBottomLabel())) {
            bottomLabel.setText(productItem.getBottomLabel());
            bottomLabel.setVisibility(View.VISIBLE);
        } else {
            bottomLabel.setVisibility(View.GONE);
        }
        title.setText(MethodChecker.fromHtml(productItem.getProductName()));
        String priceText = !TextUtils.isEmpty(productItem.getPriceRange()) ?
                productItem.getPriceRange() : productItem.getPrice();
        price.setText(priceText);
        if (productItem.getShopCity() != null && !productItem.getShopCity().isEmpty()) {
            if (isBadgesExist(productItem)) {
                location.setText(" \u2022 " + MethodChecker.fromHtml(productItem.getShopCity()));
            } else {
                location.setText(MethodChecker.fromHtml(productItem.getShopCity()));
            }
            location.setVisibility(View.VISIBLE);
        } else {
            location.setVisibility(View.GONE);
        }

        setImageProduct(productItem);
        productImage.setViewHintListener(productItem, new ImpressedImageView.ViewHintListener() {
            @Override
            public void onViewHint() {
                new ImpresionTask().execute(productItem.getTopadsImpressionUrl());
            }
        });
        wishlistButtonContainer.setVisibility(View.VISIBLE);
        wishlistButton.setBackgroundResource(R.drawable.ic_wishlist);

        if (productItem.isWishlisted()) {
            wishlistButton.setBackgroundResource(R.drawable.ic_wishlist_red);
        } else {
            wishlistButton.setBackgroundResource(R.drawable.ic_wishlist);
        }

        wishlistButtonContainer.setEnabled(productItem.isWishlistButtonEnabled());

        wishlistButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productItem.isWishlistButtonEnabled()) {
                    itemClickListener.onWishlistButtonClicked(productItem);
                }
            }
        });

        container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onLongClick(productItem,getAdapterPosition());
                return true;
            }
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(productItem, getAdapterPosition());
                new ImpresionTask().execute(productItem.getTopadsClickUrl());
            }
        });

        if (productItem.getRating()!=0) {
            ratingReviewContainer.setVisibility(View.VISIBLE);
            rating.setImageResource(
                    RatingView.getRatingDrawable(Math.round(productItem.getRating()))
            );
            reviewCount.setText("(" + productItem.getCountReview() + ")");
        } else {
            ratingReviewContainer.setVisibility(View.GONE);
        }
        if(productItem.isNew()){
            newLabel.setVisibility(View.VISIBLE);
        } else {
            newLabel.setVisibility(View.GONE);
        }
        renderBadges(productItem.getBadgesList());
    }

    private boolean isBadgesExist(ProductItem productItem) {
        List<BadgeItem> badgesList = productItem.getBadgesList();
        if (badgesList == null || badgesList.isEmpty()) {
            return false;
        }

        for (BadgeItem badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                return true;
            }
        }
        return false;
    }

    protected void renderBadges(List<BadgeItem> badgesList) {
        badgesContainer.removeAllViews();
        for (BadgeItem badgeItem : badgesList) {
            if (badgeItem.isShown()) {
                LuckyShopImage.loadImage(context, badgeItem.getImageUrl(), badgesContainer);
            }
        }
    }

    public void setImageProduct(ProductItem productItem) {
        ImageHandler.loadImageSourceSize(context, productImage, productItem.getImageUrl());
    }

}