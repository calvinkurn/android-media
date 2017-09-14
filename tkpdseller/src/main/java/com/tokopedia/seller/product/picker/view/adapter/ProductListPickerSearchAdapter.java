package com.tokopedia.seller.product.picker.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerSearchAdapter extends BaseMultipleCheckListAdapter<ProductListPickerViewModel> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductListPickerViewModel.TYPE:
                return new ProductListPickerSearchViewHolder(
                        getLayoutView(parent, R.layout.item_product_list_picker_search));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}
