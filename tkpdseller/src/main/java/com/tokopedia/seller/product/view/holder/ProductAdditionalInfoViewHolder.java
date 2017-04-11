package com.tokopedia.seller.product.view.holder;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.product.view.widget.CounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerCounterInputView;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductAdditionalInfoViewHolder {

    private TextInputLayout descriptionTextInputLayout;
    private EditText descriptionEditText;

    public ProductAdditionalInfoViewHolder(View view) {
        descriptionTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        descriptionEditText = (EditText) view.findViewById(R.id.spinner_counter_input_view_weight);
    }
}
