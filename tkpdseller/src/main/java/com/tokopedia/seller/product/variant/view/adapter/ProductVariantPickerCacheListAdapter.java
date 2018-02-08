package com.tokopedia.seller.product.variant.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseItemPickerCacheAdapter;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseItemPickerCacheViewHolder;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerCacheViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantPickerCacheListAdapter extends BaseItemPickerCacheAdapter<ProductVariantViewModel> implements BaseItemPickerCacheViewHolder.RemoveCallback<ProductVariantViewModel>{

    private int variantIdentifier;
    public ProductVariantPickerCacheListAdapter(int identifier){
        this.variantIdentifier = identifier;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductVariantViewModel.TYPE:
                ProductVariantItemPickerCacheViewHolder productVariantItemPickerCacheViewHolder =
                        new ProductVariantItemPickerCacheViewHolder(
                                variantIdentifier == ProductVariantConstant.COLOR_ID,
                                getLayoutView(parent, R.layout.item_base_cache_chip));
                productVariantItemPickerCacheViewHolder.setRemoveCallback(this);
                return productVariantItemPickerCacheViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}