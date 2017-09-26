package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ManageProductListAdapter extends BaseListAdapter<ProductManageViewModel> implements ManageProductListViewHolder.ClickOptionCallbackHolder {

    public interface ClickOptionCallback{
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ClickOptionCallback clickOptionCallback;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductManageViewModel.TYPE:
                ManageProductListViewHolder manageProductListViewHolder = new ManageProductListViewHolder(
                        getLayoutView(parent, R.layout.item_manage_product_list));
                manageProductListViewHolder.setClickOptionCallbackHolder(this);
                return manageProductListViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
     }

    public void setClickOptionCallback(ClickOptionCallback clickOptionCallback) {
        this.clickOptionCallback = clickOptionCallback;
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        if(clickOptionCallback != null){
            clickOptionCallback.onClickOptionItem(productManageViewModel);
        }
    }
}
