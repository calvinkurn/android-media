package com.tokopedia.seller.product.view.adapter.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.CategoryViewModel;

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
    }

    @Override
    public void renderData(CategoryViewModel categoryViewModel, boolean isSelected, int level) {
        super.renderData(categoryViewModel, isSelected, level);
        radioButton.setChecked(isSelected);
    }

    public interface CategoryItemViewHolderListener{

        void selectItemCategory(int categoryId);
    }

}
