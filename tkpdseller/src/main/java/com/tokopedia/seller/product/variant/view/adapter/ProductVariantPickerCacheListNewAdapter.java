package com.tokopedia.seller.product.variant.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseItemPickerCacheAdapter;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerCacheNewViewHolder;
/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantPickerCacheListNewAdapter extends BaseItemPickerCacheAdapter<ProductVariantOption>
        implements BaseItemPickerCacheViewHolder.RemoveCallback<ProductVariantOption>{

    private boolean isColorType;
    public ProductVariantPickerCacheListNewAdapter(boolean isColorType){
        this.isColorType = isColorType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductVariantOption.TYPE:
                ProductVariantItemPickerCacheNewViewHolder productVariantItemPickerCacheNewViewHolder =
                        new ProductVariantItemPickerCacheNewViewHolder(
                                isColorType,
                                getLayoutView(parent, R.layout.item_base_cache_chip));
                productVariantItemPickerCacheNewViewHolder.setRemoveCallback(this);
                return productVariantItemPickerCacheNewViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}