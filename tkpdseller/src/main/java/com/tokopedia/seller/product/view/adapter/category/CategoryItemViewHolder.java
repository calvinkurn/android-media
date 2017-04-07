package com.tokopedia.seller.product.view.adapter.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

class CategoryItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView categoryName;
    private final Context context;

    public CategoryItemViewHolder(View view) {
        super(view);
        this.context = view.getContext();
        categoryName = (TextView) view.findViewById(R.id.category_name);
    }

    public void renderData(CategoryViewModel categoryViewModel, int level) {
        categoryName.setText(categoryViewModel.getName());
        setIndentation(level);

    }

    public void setIndentation(int level) {
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) categoryName.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dp = level * 16;
        int marginStart = (int) ((dp * displayMetrics.density) + 0.5);
        layoutParams.setMargins(marginStart, 0, 0,0);
    }
}
