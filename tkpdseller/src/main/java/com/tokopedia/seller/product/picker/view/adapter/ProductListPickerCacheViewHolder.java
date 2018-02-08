package com.tokopedia.seller.product.picker.view.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;
import com.tokopedia.seller.common.utils.CircleTransform;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

class ProductListPickerCacheViewHolder extends BaseItemPickerCacheViewHolder<ProductListPickerViewModel> {
    public ProductListPickerCacheViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindObject(ProductListPickerViewModel productListPickerViewModel) {
        super.bindObject(productListPickerViewModel);
        Glide.with(imageView.getContext()).load(productListPickerViewModel.getIcon())
                .transform(new CircleTransform(imageView.getContext())).into(imageView);
    }
}
