package com.tokopedia.seller.product.category.view.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tokopedia.seller.R;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

class CategoryParentViewHolder extends CategoryPickerViewHolder  {
    private final ImageView imageChevron;
    private final ProgressBar progressBar;

    public CategoryParentViewHolder(View view, final CategoryParentViewHolderListener listener) {
        super(view);
        imageChevron = (ImageView) view.findViewById(R.id.category_picker_chevron);
        progressBar = (ProgressBar) view.findViewById(R.id.category_picker_progress_bar);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    listener.unselectParent();
                } else {
                    listener.selectParent(getCategoryId());
                    progressBar.setVisibility(View.VISIBLE);
                    imageChevron.setVisibility(View.GONE);
                }
            }
        });
    }

    public void renderData(CategoryViewModel categoryViewModel, boolean isSelected, int level) {
        super.renderData(categoryViewModel, isSelected, level);
        progressBar.setVisibility(View.GONE);
        imageChevron.setVisibility(View.VISIBLE);
        if (isSelected){
            setView(context.getResources().getColor(R.color.tkpd_main_green), context
                    .getResources()
                    .getDrawable(R.drawable.chevron_up));
        } else {
            setView(context.getResources().getColor(R.color.font_black_primary_70), context
                    .getResources()
                    .getDrawable(R.drawable.chevron_down));
        }
    }

    private void setView(int color, Drawable drawable) {
        categoryName.setTextColor(color);
        imageChevron.setImageDrawable(
                drawable
        );
    }

    public interface CategoryParentViewHolderListener {
        void selectParent(long selected);

        void unselectParent();

    }
}
