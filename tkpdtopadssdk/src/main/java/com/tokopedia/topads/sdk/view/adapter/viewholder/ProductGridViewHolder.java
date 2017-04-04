package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
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
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductGridViewModel;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class ProductGridViewHolder extends AbstractViewHolder<ProductGridViewModel> implements
        View.OnClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_ads_product_grid;
    private static final String TAG = ProductGridViewHolder.class.getSimpleName();

    private LocalAdsClickListener itemClickListener;
    private Data data;
    private Context context;
    public LinearLayout badgeContainer;
    public FlowLayout labelContainer;
    public TextView productName;
    public TextView productPrice;
    public TextView shopName;
    public TextView shopLocation;
    public ImageView productImage;
    private ImageLoader imageLoader;


    public ProductGridViewHolder(View itemView, ImageLoader imageLoader, LocalAdsClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.itemClickListener = itemClickListener;
        this.imageLoader = imageLoader;
        context = itemView.getContext();
        badgeContainer = (LinearLayout) itemView.findViewById(R.id.badges_container);
        labelContainer = (FlowLayout) itemView.findViewById(R.id.label_container);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.title);
        productPrice = (TextView) itemView.findViewById(R.id.price);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopLocation = (TextView) itemView.findViewById(R.id.location);
    }

    @Override
    public void bind(ProductGridViewModel element) {
        data = element.getData();
        if(data.getProduct()!=null){
            bindProduct(data.getProduct());
        }
        if(data.getShop()!=null) {
            bindShop(data.getShop());
        }
    }

    private void bindShop(Shop shop) {
        shopLocation.setText(shop.getLocation());
        shopName.setText(shop.getName());
        if(shop.getBadges() !=null){
//            BadgeLoader.initBadge(context, badgeContainer, shop.getBadges());
            imageLoader.loadBadge(badgeContainer, shop.getBadges());
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
        if(product.getLabels()!=null){
            LabelLoader.initLabel(context, labelContainer, product.getLabels());
        }
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener!=null) {
            itemClickListener.onProductItemClicked(getAdapterPosition(), data);
        }
    }


}
