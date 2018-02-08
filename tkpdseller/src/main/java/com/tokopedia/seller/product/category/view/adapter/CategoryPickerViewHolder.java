package com.tokopedia.seller.product.category.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/7/17.
 */

public abstract class CategoryPickerViewHolder extends RecyclerView.ViewHolder{
    protected final TextView categoryName;
    protected final Context context;
    protected boolean isSelected;
    private long categoryId;

    public CategoryPickerViewHolder(View view) {
        super(view);
        context = view.getContext();
        categoryName = (TextView) view.findViewById(R.id.category_name);
    }

    protected long getCategoryId() {
        if (categoryId != -1) {
            return categoryId;
        } else {
            throw new RuntimeException("index is missing");
        }
    }

    public void renderData(CategoryViewModel categoryViewModel, boolean isSelected, int level) {
        setIndentation(level);
        categoryName.setText(categoryViewModel.getName());
        categoryId = categoryViewModel.getId();
        this.isSelected = isSelected;
    }

    public void setIndentation(int level) {
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) categoryName.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dp = level * 16;
        int marginStart = (int) ((dp * displayMetrics.density) + 0.5);
        layoutParams.setMargins(marginStart, 0, 0,0);
    }
}
