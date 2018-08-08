package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageSortAdapter extends BaseListAdapter<ProductManageSortModel> implements ProductManageSortViewHolder.ListenerCheckedSort {

    @SortProductOption
    private String sortProductOption = SortProductOption.POSITION;

    public void setSortProductOption(String sortProductOption) {
        this.sortProductOption = sortProductOption;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductManageSortModel.TYPE:
                ProductManageSortViewHolder productManageSortViewHolder = new ProductManageSortViewHolder(
                        getLayoutView(parent, R.layout.item_product_manage_list_sort));
                productManageSortViewHolder.setListenerCheckedSort(this);
                return productManageSortViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public boolean isItemChecked(@SortProductOption String productSortId) {
        return productSortId.equals(sortProductOption);
    }
}
