package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageListAdapter extends BaseMultipleCheckListAdapter<ProductManageViewModel> implements ProductManageListViewHolder.ClickOptionCallbackHolder {


    public interface ClickOptionCallback {
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ClickOptionCallback clickOptionCallback;

    private List<String> featuredProduct;
    private boolean isActionMode;

    public void setFeaturedProduct(List<String> featuredProduct) {
        this.featuredProduct = featuredProduct;
    }

    public List<String> getFeaturedProduct() {
        return featuredProduct;
    }

    public ProductManageListAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductManageViewModel.TYPE:
                ProductManageListViewHolder productManageListViewHolder = new ProductManageListViewHolder(
                        getLayoutView(parent, R.layout.item_manage_product_list));
                productManageListViewHolder.setClickOptionCallbackHolder(this);
                return productManageListViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected void bindData(int position, RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        ((ProductManageListViewHolder) viewHolder).bindFeaturedProduct(isFeaturedProduct(data.get(position).getProductId()));
        ((ProductManageListViewHolder) viewHolder).bindActionMode(isActionMode);
    }

    public void setClickOptionCallback(ClickOptionCallback clickOptionCallback) {
        this.clickOptionCallback = clickOptionCallback;
    }

    @Override
    public void onClickOptionItem(ProductManageViewModel productManageViewModel) {
        if (clickOptionCallback != null) {
            clickOptionCallback.onClickOptionItem(productManageViewModel);
        }
    }

    public void setActionMode(boolean actionMode) {
        isActionMode = actionMode;
    }

    private boolean isFeaturedProduct(String productId) {
        return featuredProduct != null && featuredProduct.contains(productId);
    }

    public void updatePrice(String productId, String price, String currencyId, String priceCurrency) {
        int i = 0;
        for (Iterator<ProductManageViewModel> it = data.iterator(); it.hasNext(); ) {
            ProductManageViewModel productManageViewModel = it.next();
            if (productManageViewModel.getId().equalsIgnoreCase(productId)) {
                productManageViewModel.setProductPricePlain(price);
                productManageViewModel.setProductCurrencyId(Integer.parseInt(currencyId));
                productManageViewModel.setProductCurrencySymbol(priceCurrency);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }

    public void updateCashback(String productId, int cashback) {
        int i = 0;
        for (Iterator<ProductManageViewModel> it = data.iterator(); it.hasNext(); ) {
            ProductManageViewModel productManageViewModel = it.next();
            if (productManageViewModel.getId().equalsIgnoreCase(productId)) {
                productManageViewModel.setProductCashback(cashback);
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }
}
