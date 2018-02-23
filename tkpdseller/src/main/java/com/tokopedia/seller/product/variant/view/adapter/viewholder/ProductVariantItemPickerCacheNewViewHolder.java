package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;
import com.tokopedia.seller.common.utils.CircleTransform;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantItemPickerCacheNewViewHolder extends BaseItemPickerCacheViewHolder<ProductVariantOption> {

    private boolean isColorType;

    public ProductVariantItemPickerCacheNewViewHolder(boolean isColorType, View itemView) {
        super(itemView);
        this.isColorType = isColorType;
    }

    @Override
    public void bindObject(final ProductVariantOption productVariantOption) {
        super.bindObject(productVariantOption);
        if (isColorType) {
            if (!TextUtils.isEmpty(productVariantOption.getHexCode())) {
                imageView.setColorFilter(Color.parseColor(productVariantOption.getHexCode()), PorterDuff.Mode.SRC_ATOP);
                imageView.setImageResource(R.drawable.circle_white);
                imageView.setVisibility(View.VISIBLE);
            } else { // no hex
                imageView.setImageResource(R.drawable.circle_white_strike);
                imageView.clearColorFilter();
                imageView.setVisibility(View.VISIBLE);
            }
        } else {
            if (!TextUtils.isEmpty(productVariantOption.getIcon())) {
                imageView.clearColorFilter();
                Glide.with(imageView.getContext()).load(productVariantOption.getIcon())
                        .transform(new CircleTransform(imageView.getContext())).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else { // no url
                imageView.setVisibility(View.GONE);
            }
        }
    }
}