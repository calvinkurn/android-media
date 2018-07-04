package com.tokopedia.topads.sdk.view.adapter.viewholder.discovery;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.LabelLoader;
import com.tokopedia.topads.sdk.view.FlowLayout;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductListViewModel;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductListViewHolder extends AbstractViewHolder<ProductListViewModel> implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_produt_list;
    private static final String TAG = ProductListViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private Data data;
    private Context context;
    public LinearLayout badgeContainer;
    public FlowLayout labelContainer;
    public TextView productName;
    public TextView productPrice;
    public TextView shopLocation;
    public ImageView productImage;
    private ImageLoader imageLoader;
    private ImageView rating;
    private TextView reviewCount;
    private int clickPosition;

    public ProductListViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener itemClickListener, int clickPosition) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        this.clickPosition = clickPosition;
        context = itemView.getContext();
        badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        labelContainer = (FlowLayout) itemView.findViewById(R.id.label_container);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
        shopLocation = (TextView) itemView.findViewById(R.id.location);
        rating = (ImageView) itemView.findViewById(R.id.rating);
        reviewCount = (TextView) itemView.findViewById(R.id.review_count);
        ((LinearLayout) itemView.findViewById(R.id.container)).setOnClickListener(this);
    }

    @Override
    public void bind(ProductListViewModel element) {
        data = element.getData();
        Product product = data.getProduct();
        if (product != null) {
            imageLoader.loadImage(product.getImage().getS_ecs(), product.getImage().getS_url(),
                    productImage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productName.setText(Html.fromHtml(product.getName(),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                productName.setText(Html.fromHtml(product.getName()));
            }
            productPrice.setText(product.getPriceFormat());

            if (data.getProduct().getProductRating() == 0) {
                rating.setVisibility(View.GONE);
                reviewCount.setVisibility(View.GONE);
            } else {
                rating.setVisibility(View.VISIBLE);
                reviewCount.setVisibility(View.VISIBLE);
                rating.setImageResource(
                        ImageLoader.getRatingDrawable(getStarCount(data.getProduct().getProductRating()))
                );
                reviewCount.setText("(" + data.getProduct().getCountReviewFormat() + ")");
            }

            labelContainer.removeAllViews();
            if (data.getProduct().getProductRating() == 0) {
                rating.setVisibility(View.GONE);
                reviewCount.setVisibility(View.GONE);
                if (data.getProduct().isProductNewLabel()) {
                    TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_new_label,
                            null, false);
                    labelContainer.addView(label);
                }
            } else {
                rating.setVisibility(View.VISIBLE);
                reviewCount.setVisibility(View.VISIBLE);
                rating.setImageResource(
                        ImageLoader.getRatingDrawable(getStarCount(data.getProduct().getProductRating()))
                );
                reviewCount.setText("(" + data.getProduct().getCountReviewFormat() + ")");
            }
            if (product.getTopLabels() != null) {
                for (String l : product.getTopLabels()) {
                    TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_top_label,
                            null, false);
                    label.setText(l);
                    labelContainer.addView(label);
                }
            }
            if (product.getBottomLabels() != null) {
                for (String l : product.getBottomLabels()) {
                    TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_bottom_label,
                            null, false);
                    label.setText(l);
                    labelContainer.addView(label);
                }
            }
        }
        Shop shop = data.getShop();
        if (shop != null) {
            if (shop.getBadges() != null && !shop.getLocation().isEmpty()) {
                imageLoader.loadBadge(badgeContainer, shop.getBadges());
                shopLocation.setText(String.format(" \u2022 %s", shop.getLocation()));
            } else {
                shopLocation.setText(shop.getLocation());
            }
        }
    }

    private int getStarCount(int rating) {
        return Math.round(rating / 20f);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null && v.getId() == R.id.container) {
            itemClickListener.onProductItemClicked(clickPosition, data);
        }
    }
}
