package com.tokopedia.seller.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

/**
 * Created by sebastianuskh on 4/4/17.
 */

class CategoryItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView categoryName;

    public CategoryItemViewHolder(View view) {
        super(view);
        categoryName = (TextView) view.findViewById(R.id.category_name);
    }

    public void renderData(CategoryViewModel categoryViewModel) {
        categoryName.setText(categoryViewModel.getName());

    }
}
