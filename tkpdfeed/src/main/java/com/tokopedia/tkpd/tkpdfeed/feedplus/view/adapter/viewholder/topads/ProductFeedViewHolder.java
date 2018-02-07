package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Data;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads.Product;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.LocalAdsClickListener;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.ProductFeedTopAdsViewModel;

/**
 * Created by errysuprayogi on 3/27/17.
 * Copied to feed by milhamj 1/18/17.
 */

public class ProductFeedViewHolder extends AbstractViewHolder<ProductFeedTopAdsViewModel> implements
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


    public ProductFeedViewHolder(View itemView, LocalAdsClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.itemClickListener = itemClickListener;
        context = itemView.getContext();
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
    }

    @Override
    public void bind(ProductFeedTopAdsViewModel element) {
        data = element.getData();
        if (data.getProduct() != null) {
            bindProduct(data.getProduct());
        }
    }

    private void bindProduct(final Product product) {
        ImageHandler.LoadImage(productImage, product.getImage().getS_ecs());
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
