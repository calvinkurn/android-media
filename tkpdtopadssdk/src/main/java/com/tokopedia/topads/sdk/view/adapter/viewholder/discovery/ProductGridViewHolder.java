package com.tokopedia.topads.sdk.view.adapter.viewholder.discovery;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
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
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductGridViewModel;

import org.w3c.dom.Text;

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
    public TextView shopLocation;
    public ImageView productImage;
    private ImageLoader imageLoader;
    private ImageView rating;
    private TextView newLabelTxt;
    private TextView reviewCount;


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
        shopLocation = (TextView) itemView.findViewById(R.id.location);
        rating = (ImageView) itemView.findViewById(R.id.rating);
        reviewCount = (TextView) itemView.findViewById(R.id.review_count);
        newLabelTxt = itemView.findViewById(R.id.new_label);
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
        if(shop.getBadges() !=null){
            imageLoader.loadBadge(badgeContainer, shop.getBadges());
            SpannableString loc = new SpannableString(shop.getLocation());
            loc.setSpan(new BulletSpan(20, Color.GRAY), 0,
                    shop.getLocation().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            shopLocation.setText(loc);
        } else {
            shopLocation.setText(shop.getLocation());
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

        if (data.getProduct().getProductRating() == 0) {
            rating.setVisibility(View.GONE);
            reviewCount.setVisibility(View.GONE);
            if(data.getProduct().isProductNewLabel()){
                newLabelTxt.setVisibility(View.VISIBLE);
            } else {
                newLabelTxt.setVisibility(View.GONE);
            }
        } else {
            newLabelTxt.setVisibility(View.GONE);
            rating.setVisibility(View.VISIBLE);
            reviewCount.setVisibility(View.VISIBLE);
            rating.setImageResource(
                    ImageLoader.getRatingDrawable(getStarCount(data.getProduct().getProductRating()))
            );
            reviewCount.setText("(" + data.getProduct().getCountReviewFormat() + ")");
        }
    }

    private int getStarCount(int rating) {
        return Math.round(rating / 20f);
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener!=null) {
            itemClickListener.onProductItemClicked(getAdapterPosition(), data);
        }
    }


}
