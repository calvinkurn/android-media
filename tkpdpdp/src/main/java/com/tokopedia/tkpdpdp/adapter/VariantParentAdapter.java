package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpdpdp.R;

import java.util.ArrayList;

/**
 * Created by alifa on 8/21/17.
 */

public class VariantParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VARIANT_TITLE = -1;

    private Context context;
    private final ProductVariant variant;

    public VariantParentAdapter(Context context, ProductVariant variant) {
        this.context = context;
        this.variant = variant;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VARIANT_TITLE:
                return new VariantTitleViewHolder(LayoutInflater.from(context).inflate(
                        R.layout.variant_title_item, parent, false));
            default:
                return new VariantTitleViewHolder(LayoutInflater.from(context).inflate(
                        R.layout.variant_title_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return VARIANT_TITLE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VARIANT_TITLE:
            default:
                ((VariantTitleViewHolder) holder).bindData(variant);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    static class VariantTitleViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;

        VariantTitleViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.variant_product_name);
            productPrice = (TextView) itemView.findViewById(R.id.variant_product_price);
            productImage = (ImageView) itemView.findViewById(R.id.variant_image_title);
        }

        public void bindData(ProductVariant productVariant) {
            productName.setText(productVariant.getProductName());
            productPrice.setText(productVariant.getProductPrice());
            ImageHandler.LoadImage(productImage,productVariant.getProductImageUrl());
        }
    }
}


