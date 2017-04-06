package com.tokopedia.seller.product.view.adapter.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryPickerAdapter extends BaseLinearRecyclerViewAdapter implements CategoryParentViewHolder.CategoryParentViewHolderListener {
    private static final int CATEGORY_PARENT = 1000;
    private static final int CATEGORY_ITEM = 2000;
    private static final int UNSELECTED = -1;
    public static final int SELECTED_ITEM_COUNT = 1;
    private List<CategoryViewModel> data;
    private int selected = UNSELECTED;

    public CategoryPickerAdapter() {
        data = new ArrayList<>();
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
                boolean isNotSelected = selected == UNSELECTED;
                int modifiedPos = isNotSelected ? position : selected;
                ((CategoryParentViewHolder)holder)
                        .renderData(data.get(modifiedPos), position, isNotSelected);
                break;
            case CATEGORY_ITEM:
                ((CategoryItemViewHolder)holder).renderData(data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else if (data.get(position).isHasChild()){
            return CATEGORY_PARENT;
        } else {
            return CATEGORY_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (selected == UNSELECTED) {
            return data.size() + super.getItemCount();
        } else {
            return SELECTED_ITEM_COUNT;
        }
    }

    public void renderItems(List<CategoryViewModel> map) {
        data = map;
        notifyDataSetChanged();
    }

    @Override
    public void selectParent(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public void unselectParent() {
        this.selected = UNSELECTED;
        notifyDataSetChanged();
    }
}
