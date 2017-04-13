package com.tokopedia.seller.product.view.holder;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder {

    private final AddUrlContainerViewHolder addUrlContainerViewHolder;
    private TextInputLayout descriptionTextInputLayout;
    private EditText descriptionEditText;

    public ProductAdditionalInfoViewHolder(View view) {
        descriptionTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);

        addUrlContainerViewHolder = new AddUrlContainerViewHolder(view);
    }

    public void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel) {
        addUrlContainerViewHolder.addAddUrlVideModel(addUrlVideoModel);
    }


}
