package com.tokopedia.seller.product.variant.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.dialog.BaseTextPickerDialogFragment;

import java.util.ArrayList;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class ProductVariantItemPickerAddDialogFragment extends BaseTextPickerDialogFragment {

    public static final String EXTRA_VARIANT_TITLE = "EXTRA_VARIANT_TITLE";
    public static final String EXTRA_VARIANT_RESERVED_TITLE_LIST = "EXTRA_VARIANT_RESERVED_TITLE_LIST";

    private String variantTitle;
    private ArrayList<String> reservedTitleList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variantTitle = getArguments().getString(EXTRA_VARIANT_TITLE);
        reservedTitleList = getArguments().getStringArrayList(EXTRA_VARIANT_RESERVED_TITLE_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        stringPickerTitle.setText(getString(R.string.product_variant_item_picker_add_variant_title_dialog, variantTitle));
        textInputLayout.setHint(getString(R.string.product_variant_item_picker_add_variant_hint_dialog, variantTitle));
        return view;

    }

    @Override
    protected void onTextSubmited(String text) {
        text = text.trim();
        if (TextUtils.isEmpty(text)) {
            textInputLayout.setError(getString(R.string.product_variant_item_picker_add_variant_error_name_empty));
            return;
        }
        for (String reservedTitle: reservedTitleList) {
            if (reservedTitle.equalsIgnoreCase(text)) {
                textInputLayout.setError(getString(R.string.product_variant_item_picker_add_variant_error_name_existed));
                return;
            }
        }
        super.onTextSubmited(text);
    }
}