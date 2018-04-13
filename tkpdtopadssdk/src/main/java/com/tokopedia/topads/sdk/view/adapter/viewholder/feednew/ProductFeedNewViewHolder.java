package com.tokopedia.topads.sdk.view.adapter.viewholder.feednew;

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
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ProductFeedNewViewModel;

/**
 * @author by milhamj on 29/03/18.
 */

public class ProductFeedNewViewHolder extends AbstractViewHolder<ProductFeedNewViewModel>
        implements View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_feed_new;

    private Data data;
    public TextView productName;
    public ImageView productImage;
    private Context context;
    private ImageLoader imageLoader;
    private LocalAdsClickListener itemClickListener;

    public ProductFeedNewViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener
            itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.context = itemView.getContext();
        this.imageLoader = imageLoader;
        this.itemClickListener = itemClickListener;

        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onProductItemClicked(getAdapterPosition(), data);
        }
    }

    @Override
    public void bind(ProductFeedNewViewModel element) {
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
    }
}
