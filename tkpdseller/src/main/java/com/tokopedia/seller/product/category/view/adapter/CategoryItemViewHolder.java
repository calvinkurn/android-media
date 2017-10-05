package com.tokopedia.seller.product.category.view.adapter;

import android.view.View;
import android.widget.RadioButton;

import com.tokopedia.seller.R;
import com.tokopedia.core.common.category.view.model.CategoryViewModel;

/**
 * @author sebastianuskh on 4/4/17.
 */

class CategoryItemViewHolder extends CategoryPickerViewHolder {

    private final RadioButton radioButton;

    public CategoryItemViewHolder(View view, final CategoryItemViewHolderListener listener) {
        super(view);
        radioButton = (RadioButton) view.findViewById(R.id.category_picker_radio_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectItemCategory(getCategoryId());
            }
        });
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectItemCategory(getCategoryId());
            }
        });
    }

    @Override
    public void renderData(CategoryViewModel categoryViewModel, boolean isSelected, int level) {
        super.renderData(categoryViewModel, isSelected, level);
        radioButton.setChecked(isSelected);
    }

    public interface CategoryItemViewHolderListener{

        void selectItemCategory(long categoryId);
    }

}
