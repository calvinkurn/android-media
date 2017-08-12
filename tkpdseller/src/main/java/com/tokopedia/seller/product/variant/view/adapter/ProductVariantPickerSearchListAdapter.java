package com.tokopedia.seller.product.variant.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.viewholder.ItemPickerTypeViewHolder;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerSearchViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantPickerSearchListAdapter extends BaseListAdapter<ProductVariantViewModel> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductVariantViewModel.TYPE:
                return new ProductVariantItemPickerSearchViewHolder(getLayoutView(parent, R.layout.item_product_variant_picker_search));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}