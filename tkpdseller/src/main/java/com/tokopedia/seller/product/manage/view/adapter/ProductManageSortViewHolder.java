package com.tokopedia.seller.product.manage.view.adapter;

import android.view.View;

import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public class ProductManageSortViewHolder extends BaseViewHolder<ProductManageSortModel> {

    public interface ListenerCheckedSort{
        boolean isItemChecked(@SortProductOption String productSortId);
    }

    ListenerCheckedSort listenerCheckedSort;

    public ProductManageSortViewHolder(View layoutView) {
        super(layoutView);
    }

    public void setListenerCheckedSort(ListenerCheckedSort listenerCheckedSort) {
        this.listenerCheckedSort = listenerCheckedSort;
    }

    @Override
    public void bindObject(ProductManageSortModel productManageSortModel) {
        boolean isItemChecked;
        if(listenerCheckedSort != null){
            isItemChecked = listenerCheckedSort.isItemChecked(productManageSortModel.getId());
        }
    }
}
