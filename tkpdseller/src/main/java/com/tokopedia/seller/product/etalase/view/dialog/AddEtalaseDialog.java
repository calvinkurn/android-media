package com.tokopedia.seller.product.etalase.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.dialog.BaseTextPickerDialogFragment;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class AddEtalaseDialog extends BaseTextPickerDialogFragment {

    public static final String TAG = "AddEtalaseDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        stringPickerTitle.setText(getString(R.string.product_etalase_picker_add_etalase_title_dialog));
        textInputLayout.setHint(getString(R.string.product_etalase_picker_add_etalase_name_hint));
        return view;

    }

    @Override
    protected void onTextSubmitted(String text) {
        if(text.trim().isEmpty()){
            textInputLayout.setError(getString(R.string.product_etalase_picker_add_etalase_name_empty));
        } else if (text.trim().length() < 3) {
            textInputLayout.setError(getString(R.string.product_etalase_picker_add_etalase_name_too_short));
        } else {
            super.onTextSubmitted(text);
        }
    }
}
