package com.tokopedia.topads.sdk.view.adapter.viewholder.feed;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ProductFeedViewModel;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductFeedViewHolder extends AbstractViewHolder<ProductFeedViewModel> implements
        View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_feed;
    private static final String TAG = ProductFeedViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private Data data;
    private Context context;
    public TextView productName;
    public TextView productPrice;
    public ImageView productImage;
    private ImageLoader imageLoader;


    public ProductFeedViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        context = itemView.getContext();
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
    }

    @Override
    public void bind(ProductFeedViewModel element) {
        data = element.getData();
        if (data.getProduct() != null) {
            bindProduct(data.getProduct());
        }
    }


    private void bindProduct(final Product product) {
        imageLoader.loadImage(product.getImage().getS_ecs(), product.getImage().getS_url(),
                productImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            productName.setText(Html.fromHtml(product.getName(),
                    Html.FROM_HTML_MODE_LEGACY));
        } else {
            productName.setText(Html.fromHtml(product.getName()));
        }
        productPrice.setText(product.getPriceFormat());
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onProductItemClicked(getAdapterPosition(), data);
        }
    }


}
