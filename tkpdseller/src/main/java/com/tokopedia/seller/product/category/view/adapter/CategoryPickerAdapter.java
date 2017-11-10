package com.tokopedia.seller.product.category.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.common.category.view.model.CategoryViewModel;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.core.common.category.view.model.CategoryLevelViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryPickerAdapter
        extends BaseLinearRecyclerViewAdapter
        implements CategoryParentViewHolder.CategoryParentViewHolderListener, CategoryItemViewHolder.CategoryItemViewHolderListener {
    private static final int CATEGORY_PARENT = 1000;
    private static final int CATEGORY_ITEM = 2000;
    private static final int SELECTED_ITEM_COUNT = 1;
    private CategoryLevelViewModel data;
    private final CategoryPickerAdapterListener listener;

    public CategoryPickerAdapter(CategoryPickerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CATEGORY_PARENT:
                return getParentViewHolder(parent);
            case CATEGORY_ITEM:
                return getItemViewHolder(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    private RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_product_category, parent, false);
        return new CategoryItemViewHolder(view, this);
    }

    private RecyclerView.ViewHolder getParentViewHolder(ViewGroup parent) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_product_category_parent, parent, false);
        return new CategoryParentViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CATEGORY_PARENT:
                int renderedPosition = getPositionRendered(position);
                ((CategoryParentViewHolder) holder)
                        .renderData(
                                data.getViewModels().get(renderedPosition),
                                isCategorySelectedAndParent(),
                                data.getLevel()
                        );
                break;
            case CATEGORY_ITEM:
                boolean isSelected = data.getCategoryId() == data.getViewModels().get(position).getId();
                ((CategoryItemViewHolder) holder)
                        .renderData(
                                data.getViewModels().get(position), isSelected, data.getLevel()
                        );
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.getViewModels().isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else if (
                data.getViewModels()
                        .get(getPositionRendered(position))
                        .isHasChild()) {
            return CATEGORY_PARENT;
        } else {
            return CATEGORY_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (isCategorySelectedAndParent()) {
            return SELECTED_ITEM_COUNT;
        } else {
            return data.getViewModels().size() + super.getItemCount();
        }
    }

    private int getPositionRendered(int position) {
        return isCategorySelectedAndParent() ? data.getSelectedPosition() : position;
    }

    private boolean isCategorySelectedAndParent() {
        return isOneCategorySelected() && isSelectedParent();
    }

    private boolean isOneCategorySelected() {
        return data.getCategoryId() != CategoryLevelViewModel.UNSELECTED;
    }

    private boolean isSelectedParent() {
        if (data == null) {
            return false;
        }
        CategoryViewModel categoryViewModel = data.getSelectedModel();
        if (categoryViewModel == null) {
            return false;
        }
        return data.getSelectedModel().isHasChild();
    }

    public void renderItems(CategoryLevelViewModel map) {
        data = map;
        notifyDataSetChanged();
    }

    @Override
    public void selectParent(long selectedId) {
        this.listener.selectParent(selectedId);
    }

    @Override
    public void unselectParent() {
        data.setCategoryId(CategoryLevelViewModel.UNSELECTED);
        this.listener.unselectParent(data.getLevel());
        notifyDataSetChanged();
    }

    @Override
    public void selectItemCategory(long selectedId) {
        data.setCategoryId(selectedId);
        listener.selectCategoryItem();
    }
}
