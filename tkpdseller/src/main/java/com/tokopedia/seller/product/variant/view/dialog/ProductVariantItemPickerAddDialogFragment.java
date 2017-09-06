package com.tokopedia.seller.product.variant.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;

import java.util.ArrayList;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class ProductVariantItemPickerAddDialogFragment extends BaseTextPickerDialogFragment {

    public static final String EXTRA_VARIANT_TITLE = "EXTRA_VARIANT_TITLE";
    public static final String EXTRA_STRING_TO_INPUT = "EXTRA_DEFAULT_INPUT";
    public static final String EXTRA_VARIANT_RESERVED_TITLE_LIST = "EXTRA_VARIANT_RESERVED_TITLE_LIST";
    private static final int MAX_VARIANT_NAME_LENGTH = 15;

    private String variantTitle;
    private ArrayList<String> reservedTitleList;
    private String initialStringToAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variantTitle = getArguments().getString(EXTRA_VARIANT_TITLE);
        reservedTitleList = getArguments().getStringArrayList(EXTRA_VARIANT_RESERVED_TITLE_LIST);
        if (savedInstanceState == null) {
            initialStringToAdd = getArguments().getString(EXTRA_STRING_TO_INPUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        stringPickerTitle.setText(getString(R.string.product_variant_item_picker_add_variant_title_dialog, variantTitle));
        textInputLayout.setHint(getString(R.string.product_variant_item_picker_add_variant_hint_dialog, variantTitle));
        if (!TextUtils.isEmpty(initialStringToAdd)) {
            EditText editText = textInputLayout.getEditText();
            editText.setText(initialStringToAdd);
            editText.setSelection(initialStringToAdd.length());
        }
        return view;

    }

    @Override
    protected void onTextSubmitted(String text) {
        text = text.trim();
        if (TextUtils.isEmpty(text)) {
            textInputLayout.setError(getString(R.string.product_variant_item_picker_add_variant_error_name_empty));
            return;
        }
        if (text.length() > MAX_VARIANT_NAME_LENGTH) {
            textInputLayout.setError(getString(R.string.product_variant_item_picker_add_variant_error_length_to_long, MAX_VARIANT_NAME_LENGTH));
            return;
        }
        for (String reservedTitle : reservedTitleList) {
            if (reservedTitle.equalsIgnoreCase(text.trim())) {
                textInputLayout.setError(getString(R.string.product_variant_item_picker_add_variant_error_name_existed));
                return;
            }
        }
        super.onTextSubmitted(text);
    }
}