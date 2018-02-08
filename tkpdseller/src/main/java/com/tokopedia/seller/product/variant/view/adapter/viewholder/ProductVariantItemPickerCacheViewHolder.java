package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;
import com.tokopedia.seller.common.utils.CircleTransform;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantItemPickerCacheViewHolder extends BaseItemPickerCacheViewHolder<ProductVariantViewModel> {

    private boolean hasColor;

    public ProductVariantItemPickerCacheViewHolder(boolean hasColor, View itemView) {
        super(itemView);
        this.hasColor = hasColor;
    }

    @Override
    public void bindObject(final ProductVariantViewModel productVariantViewModel) {
        super.bindObject(productVariantViewModel);
        if (hasColor) {
            if (!TextUtils.isEmpty(productVariantViewModel.getHexCode())) {
                imageView.setColorFilter(Color.parseColor(productVariantViewModel.getHexCode()), PorterDuff.Mode.SRC_ATOP);
                imageView.setImageResource(R.drawable.circle_white);
                imageView.setVisibility(View.VISIBLE);
            } else { // no hex
                imageView.setImageResource(R.drawable.circle_white_strike);
                imageView.clearColorFilter();
                imageView.setVisibility(View.VISIBLE);
            }
        } else {
            if (!TextUtils.isEmpty(productVariantViewModel.getIcon())) {
                imageView.clearColorFilter();
                Glide.with(imageView.getContext()).load(productVariantViewModel.getIcon())
                        .transform(new CircleTransform(imageView.getContext())).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else { // no url
                imageView.setVisibility(View.GONE);
            }
        }
    }
}