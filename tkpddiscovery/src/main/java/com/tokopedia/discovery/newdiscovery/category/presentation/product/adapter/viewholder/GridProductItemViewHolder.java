package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.helper.IndicatorViewHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.LabelItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.tkpdpdp.customview.RatingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/11/17.
 */

public class GridProductItemViewHolder extends AbstractViewHolder<ProductItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.listview_product_item_grid;

    private ImageView productImage;
    private TextView title;
    private TextView price;
    private FlowLayout labelContainer;
    private TextView shopName;
    private TextView location;
    private LinearLayout badgesContainer;
    private ImageView wishlistButton;
    private RelativeLayout wishlistButtonContainer;
    private View container;
    private ImageView rating;
    private TextView reviewCount;
    private LinearLayout ratingReviewContainer;
    private ItemClickListener itemClickListener;
    private Context context;

    public GridProductItemViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        title = (TextView) itemView.findViewById(R.id.title);
        price = (TextView) itemView.findViewById(R.id.price);
        labelContainer = (FlowLayout) itemView.findViewById(R.id.label_container);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        location = (TextView) itemView.findViewById(R.id.location);
        badgesContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        wishlistButton = (ImageView) itemView.findViewById(R.id.wishlist_button);
        wishlistButtonContainer = (RelativeLayout) itemView.findViewById(R.id.wishlist_button_container);
        container = itemView.findViewById(R.id.container);
        rating = (ImageView) itemView.findViewById(R.id.rating);
        reviewCount = (TextView) itemView.findViewById(R.id.review_count);
        ratingReviewContainer = (LinearLayout) itemView.findViewById(R.id.rating_review_container);
        context = itemView.getContext();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bind(final ProductItem productItem) {
        title.setText(MethodChecker.fromHtml(productItem.getProductName()));
        price.setText(productItem.getPrice());
        if (productItem.getShopCity() != null)
            location.setText(MethodChecker.fromHtml(productItem.getShopCity()));
        else
            location.setVisibility(View.INVISIBLE);

        shopName.setText(MethodChecker.fromHtml(productItem.getShopName()));
        ImageHandler.loadImageThumbs(context, productImage, productItem.getImageUrl());

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
                    itemClickListener.onWishlistButtonClicked(productItem, getAdapterPosition());
                }
            }
        });

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(productItem, getAdapterPosition());
            }
        });

        if (productItem.getRating() != null && !productItem.getRating().equals("0")) {
            ratingReviewContainer.setVisibility(View.VISIBLE);
            float rateAmount = Float.parseFloat(productItem.getRating());
            rating.setImageResource(
                    RatingView.getRatingDrawable(Math.round(rateAmount))
            );
            reviewCount.setText("(" + productItem.getCountReview() + ")");
        } else {
            ratingReviewContainer.setVisibility(View.GONE);
        }

        renderBadges(productItem.getBadgesList());
        renderLabels(productItem.getLabelList());
    }

    protected void renderLabels(List<LabelItem> labelList) {
        List<String> titles = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < labelList.size(); i++) {
            titles.add(labelList.get(i).getTitle());
            colors.add(labelList.get(i).getColor());
        }
        IndicatorViewHelper.renderLabelsViewV2(context, labelContainer, titles, colors);
    }

    protected void renderBadges(List<BadgeItem> badgesList) {
        List<String> badgesImageUrl = new ArrayList<>();
        for (BadgeItem model : badgesList) {
            badgesImageUrl.add(model.getImageUrl());
        }
        IndicatorViewHelper.renderBadgesViewV2(context, badgesContainer, badgesImageUrl);
    }
}