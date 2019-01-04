package com.tokopedia.seller.product.category.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.base.list.seller.view.old.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.seller.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/6/17.
 */

public class CategoryPickerLevelAdapter extends BaseLinearRecyclerViewAdapter implements CategoryPickerAdapterListener {
    private static final int LEVEL_CATEGORY_VIEW = 1000;
    private final List<CategoryLevelViewModel> data;
    private final CategoryPickerLevelAdapterListener listener;

    public CategoryPickerLevelAdapter(CategoryPickerLevelAdapterListener listener) {
        this.listener = listener;
        data = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LEVEL_CATEGORY_VIEW) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_product_category_picker_level, parent, false);
            return new CategoryLevelPickerViewHolder(view, this);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case LEVEL_CATEGORY_VIEW:
                ((CategoryLevelPickerViewHolder) holder).renderData(data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ((data.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return LEVEL_CATEGORY_VIEW;
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    public void addLevelItem(List<CategoryViewModel> map, long categoryId) {
        int level = data.size();
        if (categoryId > 0) {
            data.get(level - 1).setCategoryId(categoryId);
        }
        data.add(new CategoryLevelViewModel(map, level));
        notifyDataSetChanged();

    }

    @Override
    public void selectParent(long categoryId) {
        listener.selectParent(categoryId);
    }

    @Override
    public void unselectParent(int level) {
        for (int i = data.size() - 1; i > level; i--) {
            data.remove(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public void selectCategoryItem() {
        List<CategoryViewModel> listSelectedCategory = new ArrayList<>();
        for (CategoryLevelViewModel levelViewModel : data) {
            CategoryViewModel categoryViewModel = levelViewModel.getSelectedModel();
            if (categoryViewModel != null) {
                listSelectedCategory.add(categoryViewModel);
            }
        }
        listener.selectSetCategory(listSelectedCategory);
    }

    public void render(List<CategoryLevelViewModel> categoryLevelDomainModels) {
        data.addAll(categoryLevelDomainModels);
        notifyDataSetChanged();
    }
}
