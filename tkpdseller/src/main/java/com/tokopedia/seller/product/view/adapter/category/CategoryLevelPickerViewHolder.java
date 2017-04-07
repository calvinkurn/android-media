package com.tokopedia.seller.product.view.adapter.category;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryLevelViewModel;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/6/17.
 */

class CategoryLevelPickerViewHolder extends RecyclerView.ViewHolder {
    private final CategoryPickerAdapter adapter;

    public CategoryLevelPickerViewHolder(View view, CategoryPickerAdapterListener listener) {
        super(view);
        RecyclerView levelRecyclerView = (RecyclerView) view.findViewById(R.id.level_recycler_view);
        levelRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new CategoryPickerAdapter(listener);
        levelRecyclerView.setAdapter(adapter);
    }

    public void renderData(CategoryLevelViewModel categoryViewModels, int level) {
        adapter.renderItems(categoryViewModels, level);
    }
}
