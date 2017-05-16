package com.tokopedia.seller.product.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class AddEtalaseDialog extends TextPickerDialog {

    public static final String TAG = "AddEtalaseDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        stringPickerTitle.setText(getString(R.string.etalase_picker_add_etalase_title_dialog));
        textInputLayout.setHint(getString(R.string.etalase_picker_add_etalase_name_hint));
        return view;

    }

    @Override
    protected void onTextSubmited(String text) {
        if(text.isEmpty()){
            textInputLayout.setError(getString(R.string.etalase_picker_add_etalase_name_empty));
        } else {
            super.onTextSubmited(text);
        }
    }
}
