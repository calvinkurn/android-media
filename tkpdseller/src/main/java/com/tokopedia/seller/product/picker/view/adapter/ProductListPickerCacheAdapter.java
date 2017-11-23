package com.tokopedia.seller.product.picker.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseItemPickerCacheAdapter;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerCacheAdapter extends BaseItemPickerCacheAdapter<ProductListPickerViewModel>  {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductListPickerViewModel.TYPE:
                ProductListPickerCacheViewHolder productListPickerCacheViewHolder =
                        new ProductListPickerCacheViewHolder(
                                getLayoutView(parent, R.layout.item_base_cache_chip));
                productListPickerCacheViewHolder.setRemoveCallback(this);
                return productListPickerCacheViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}
