package com.tokopedia.seller.product.view.adapter.category;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

class CategoryParentViewHolder extends CategoryPickerViewHolder  {
    private final ImageView imageChevron;

    public CategoryParentViewHolder(View view, final CategoryParentViewHolderListener listener) {
        super(view);
        imageChevron = (ImageView) view.findViewById(R.id.category_picker_chevron);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    listener.unselectParent();
                } else {
                    listener.selectParent(getCategoryId());
                }
            }
        });
    }

    public void renderData(CategoryViewModel categoryViewModel, boolean isSelected, int level) {
        super.renderData(categoryViewModel, isSelected, level);
        if (isSelected){
            setView(context.getResources().getColor(R.color.font_top_ads_green), context
                    .getResources()
                    .getDrawable(R.drawable.chevron_up));
        } else {
            setView(context.getResources().getColor(R.color.font_top_ads_black_70), context
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
        void selectParent(int selected);

        void unselectParent();

    }
}
