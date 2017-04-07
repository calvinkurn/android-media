package com.tokopedia.seller.product.view.adapter.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

class CategoryParentViewHolder extends RecyclerView.ViewHolder {
    private final TextView categoryName;
    private final ImageView imageChevron;
    private final Context context;
    private int index;
    private boolean isNotSelected;

    public CategoryParentViewHolder(View view, final CategoryParentViewHolderListener listener) {
        super(view);
        context = view.getContext();
        categoryName = (TextView) view.findViewById(R.id.category_name);
        imageChevron = (ImageView) view.findViewById(R.id.category_picker_chevron);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotSelected) {
                    listener.selectParent(getIndex());
                } else {
                    listener.unselectParent();
                }
            }
        });
    }

    public void renderData(CategoryViewModel categoryViewModel, int index, boolean isNotSelected, int level) {
        this.index = index;
        this.isNotSelected = isNotSelected;
        if (isNotSelected){
            categoryName.setTextColor(context.getResources().getColor(R.color.font_top_ads_black_70));
            imageChevron.setImageDrawable(
                    context
                            .getResources()
                            .getDrawable(R.drawable.chevron_down)
            );
        } else {
            categoryName.setTextColor(context.getResources().getColor(R.color.font_top_ads_green));
            imageChevron.setImageDrawable(
                    context
                            .getResources()
                            .getDrawable(R.drawable.chevron_up)
            );
        }
        categoryName.setText(categoryViewModel.getName());
        setIndentation(level);
    }

    public int getIndex() {
        if (index != -1) {
            return index;
        } else {
            throw new RuntimeException("index is missing");
        }
    }

    public void setIndentation(int level) {
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) categoryName.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dp = level * 16;
        int marginStart = (int) ((dp * displayMetrics.density) + 0.5);
        layoutParams.setMargins(marginStart, 0, 0,0);
    }

    public interface CategoryParentViewHolderListener {
        void selectParent(int selected);

        void unselectParent();

    }
}
