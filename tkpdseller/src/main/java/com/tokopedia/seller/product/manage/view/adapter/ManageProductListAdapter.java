package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ManageProductListAdapter extends BaseListAdapter<ProductManageViewModel> implements ManageProductListViewHolder.ClickOptionCallbackHolder {

    public interface ClickOptionCallback{
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ClickOptionCallback clickOptionCallback;

    private List<String> featuredProduct;

    public ManageProductListAdapter() {
        featuredProduct = new ArrayList<>();
    }

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

    @Override
    protected void bindData(int position, RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        ((ManageProductListViewHolder)viewHolder).bindFeaturedProduct(isFeaturedProduct(data.get(position).getProductId()));
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

    public void setFeaturedProduct(List<String> featuredProduct) {
        if(featuredProduct != null)
        this.featuredProduct = featuredProduct;
    }

    public boolean isFeaturedProduct(String productId){
        return featuredProduct.contains(productId);
    }
}
