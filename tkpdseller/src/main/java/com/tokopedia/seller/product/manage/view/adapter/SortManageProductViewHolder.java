package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.model.SortManageProductModel;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public class SortManageProductViewHolder extends BaseViewHolder<SortManageProductModel> {

    public interface ListenerCheckedSort{
        boolean isItemChecked(@SortProductOption String productSortId);
    }

    ListenerCheckedSort listenerCheckedSort;

    public SortManageProductViewHolder(View layoutView) {
        super(layoutView);
    }

    public void setListenerCheckedSort(ListenerCheckedSort listenerCheckedSort) {
        this.listenerCheckedSort = listenerCheckedSort;
    }

    @Override
    public void bindObject(SortManageProductModel sortManageProductModel) {
        boolean isItemChecked;
        if(listenerCheckedSort != null){
            isItemChecked = listenerCheckedSort.isItemChecked(sortManageProductModel.getId());
        }
    }
}
