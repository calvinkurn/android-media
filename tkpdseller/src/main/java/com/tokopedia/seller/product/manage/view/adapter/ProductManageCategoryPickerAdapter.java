package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.model.ProductManageCategoryViewModel;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;

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
