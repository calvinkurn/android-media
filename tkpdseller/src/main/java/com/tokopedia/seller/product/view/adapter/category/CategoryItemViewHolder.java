package com.tokopedia.seller.product.view.adapter.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/4/17.
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
