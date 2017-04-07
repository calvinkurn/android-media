package com.tokopedia.seller.product.view.adapter.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryLevelViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryPickerAdapter extends BaseLinearRecyclerViewAdapter implements CategoryParentViewHolder.CategoryParentViewHolderListener {
    private static final int CATEGORY_PARENT = 1000;
    private static final int CATEGORY_ITEM = 2000;
    public static final int SELECTED_ITEM_COUNT = 1;
    private CategoryLevelViewModel data;
    private final CategoryPickerAdapterListener listener;

    public CategoryPickerAdapter(CategoryPickerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
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
                .inflate(R.layout.category_item_view_holder, parent, false);
        return new CategoryItemViewHolder(view);
    }

    private RecyclerView.ViewHolder getParentViewHolder(ViewGroup parent) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_parent_view_holder, parent, false);
        return new CategoryParentViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case CATEGORY_PARENT:
                boolean isNotSelected = data.getSelected() == CategoryLevelViewModel.UNSELECTED;
                int renderedPosition = isNotSelected ? position : data.getSelected();
                ((CategoryParentViewHolder)holder)
                        .renderData(data.getViewModels().get(renderedPosition), position, isNotSelected);
                break;
            case CATEGORY_ITEM:
                ((CategoryItemViewHolder)holder).renderData(data.getViewModels().get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.getViewModels().isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else if (data.getViewModels().get(position).isHasChild()){
            return CATEGORY_PARENT;
        } else {
            return CATEGORY_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (data.getSelected() == CategoryLevelViewModel.UNSELECTED) {
            return data.getViewModels().size() + super.getItemCount();
        } else {
            return SELECTED_ITEM_COUNT;
        }
    }

    public void renderItems(CategoryLevelViewModel map) {
        data = map;
        notifyDataSetChanged();
    }

    @Override
    public void selectParent(int selected) {
        data.setSelected(selected);
        this.listener.selectParent(data.getViewModels().get(selected).getId());
        notifyDataSetChanged();
    }

    @Override
    public void unselectParent() {
        data.setSelected(CategoryLevelViewModel.UNSELECTED);
        this.listener.unselectParent(data.getLevel());
        notifyDataSetChanged();
    }
}
