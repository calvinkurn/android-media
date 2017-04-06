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
 * @author sebastianuskh on 4/6/17.
 */

public class CategoryLevelPickerAdapter extends BaseLinearRecyclerViewAdapter {
    private static final int LEVEL_CATEGORY_VIEW = 1000;
    private final List<List<CategoryViewModel>> data;

    public CategoryLevelPickerAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LEVEL_CATEGORY_VIEW){
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.category_picker_level_view_layout, parent, false);
            return new CategoryLevelPickerViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case LEVEL_CATEGORY_VIEW:
                ((CategoryLevelPickerViewHolder)holder).renderData(data.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if ((data.isEmpty() || isLoading() || isRetry())){
            return super.getItemViewType(position);
        } else {
            return LEVEL_CATEGORY_VIEW;
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    public void addLevelItem(List<CategoryViewModel> map) {
        data.add(map);
        notifyDataSetChanged();

    }
}
