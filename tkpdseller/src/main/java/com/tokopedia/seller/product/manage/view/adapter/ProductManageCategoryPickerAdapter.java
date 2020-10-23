package com.tokopedia.seller.product.manage.view.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.manageitem.data.cloud.model.category.ProductManageCategoryViewModel;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageCategoryPickerAdapter extends BaseListAdapter<ProductManageCategoryViewModel> implements ProductManageCategoryPickerViewHolder.ListenerCheckedCategory {

    private long idCategorySelected = ProductManageConstant.FILTER_ALL_CATEGORY;

    public void setIdCategorySelected(long idCategorySelected) {
        this.idCategorySelected = idCategorySelected;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductManageCategoryViewModel.TYPE:
                ProductManageCategoryPickerViewHolder productManageCategoryPickerViewHolder = new ProductManageCategoryPickerViewHolder(
                        getLayoutView(parent, R.layout.item_product_manage_list_sort));
                productManageCategoryPickerViewHolder.setListenerCheckedCategory(this);
                return productManageCategoryPickerViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public boolean isItemChecked(long idCategorySelected) {
        return this.idCategorySelected == idCategorySelected;
    }
}
