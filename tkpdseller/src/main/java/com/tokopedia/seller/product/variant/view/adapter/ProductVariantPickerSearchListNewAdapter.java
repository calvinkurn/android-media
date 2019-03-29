package com.tokopedia.seller.product.variant.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerSearchNewViewHolder;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantPickerSearchListNewAdapter extends BaseMultipleCheckListAdapter<ProductVariantOption> {

    private boolean isColorType;
    public ProductVariantPickerSearchListNewAdapter(boolean isColorType){
        this.isColorType = isColorType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductVariantOption.TYPE:
                return new ProductVariantItemPickerSearchNewViewHolder(
                        getLayoutView(parent, R.layout.item_product_variant_picker_search),isColorType);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}