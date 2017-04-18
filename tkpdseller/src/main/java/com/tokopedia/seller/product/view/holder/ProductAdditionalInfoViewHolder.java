package com.tokopedia.seller.product.view.holder;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductAddView;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder {

    private final AddUrlContainerViewHolder addUrlContainerViewHolder;
    private TextInputLayout descriptionTextInputLayout;
    private EditText descriptionEditText;
    private ProductAddView productAddView;

    public ProductAdditionalInfoViewHolder(View view, final ProductAddView productAddView) {
        this.productAddView = productAddView;
        descriptionTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        descriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                productAddView.updateProductScoring();
            }
        });

        addUrlContainerViewHolder = new AddUrlContainerViewHolder(view);
    }

    public void addAddUrlVideModel(AddUrlVideoModel addUrlVideoModel) {
        addUrlContainerViewHolder.addAddUrlVideModel(addUrlVideoModel);
    }

    public String getDescription() {
        return descriptionEditText.getText().toString();
    }
}
