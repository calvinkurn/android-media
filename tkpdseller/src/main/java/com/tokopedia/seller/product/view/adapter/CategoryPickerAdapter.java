package com.tokopedia.seller.product.view.adapter;

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

public class CategoryPickerAdapter extends BaseLinearRecyclerViewAdapter {
    private static final int VIEW_DATA = 1000;
    private List<CategoryViewModel> data;

    public CategoryPickerAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_DATA:
                return getViewHolder(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_picker_item_view_holder, parent, false);
        return new CategoryPickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case VIEW_DATA:
                ((CategoryPickerViewHolder)holder).renderData(data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return VIEW_DATA;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    public void renderItems(List<CategoryViewModel> map) {
        data = map;
        notifyDataSetChanged();
    }
}
