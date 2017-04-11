package com.tokopedia.seller.product.view.holder;

import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductInfoViewHolder {

    private EditText nameEditText;
    private LabelView categoryLabelView;
    private LabelView catalogLabelView;

    private String name;
    private String categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameEditText.setText(name);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ProductInfoViewHolder(View view) {
        nameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        categoryLabelView = (LabelView) view.findViewById(R.id.label_view_category);
        catalogLabelView = (LabelView) view.findViewById(R.id.label_view_category);
    }
}