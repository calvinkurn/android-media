package com.tokopedia.seller.product.variant.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerSearchViewHolder;

/**
 * @author normansyahputa on 5/26/17.
 */
@Deprecated
public class ProductVariantPickerSearchListAdapter extends BaseMultipleCheckListAdapter<ProductVariantOption> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductVariantOption.TYPE:
                return new ProductVariantItemPickerSearchViewHolder(
                        getLayoutView(parent, R.layout.item_product_variant_picker_search));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}