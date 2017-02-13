package com.tokopedia.seller.gmsubscribe.view.checkout.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMVoucherViewModel;

/**
 * Created by sebastianuskh on 1/31/17.
 */
public class CodeVoucherViewHolder {
    private final CodeVoucherViewHolderCallback callback;
    private final CheckBox checkbox;
    private final LinearLayout detail;
    private final TextInputLayout voucherTextInputLayout;
    private final EditText voucherEditText;
    private final Button voucherButton;
    private final TextView successMessage;

    public CodeVoucherViewHolder(CodeVoucherViewHolderCallback callback, View view) {
        this.callback = callback;
        this.checkbox = (CheckBox) view.findViewById(R.id.check_box_to_open_voucher_code);
        this.detail = (LinearLayout) view.findViewById(R.id.gm_code_voucher_detail);
        this.voucherTextInputLayout = (TextInputLayout) view.findViewById(R.id.gm_voucher_check_layout);
        this.voucherEditText = (EditText) view.findViewById(R.id.gm_voucher_check_edittext);
        this.voucherButton = (Button) view.findViewById(R.id.gm_voucher_code_check_button);
        this.successMessage = (TextView) view.findViewById(R.id.textview_voucher_message_success);
        voucherButton.setOnClickListener(getVoucherChecked());
        checkbox.setOnCheckedChangeListener(getVoucherCheckedListener());
    }

    public View.OnClickListener getVoucherChecked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.checkVoucher(voucherEditText.getText().toString());
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener getVoucherCheckedListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setShowVoucherCodeDetail(isChecked);
                if(isChecked) {
                    focusToVoucherEditText();
                } else {
                    dismissKeyboard();
                }
            }
        };
    }

    private void focusToVoucherEditText() {
        voucherEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) callback.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(voucherEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setShowVoucherCodeDetail(boolean isChecked) {
        int visibility = isChecked? View.VISIBLE : View.GONE;
        detail.setVisibility(visibility);
    }

    public void renderVoucherView(GMVoucherViewModel gmVoucherViewModel) {
        if(gmVoucherViewModel.isSuccess()){
            successMessage.setVisibility(View.VISIBLE);
            successMessage.setText(gmVoucherViewModel.getMessage());
            voucherTextInputLayout.setError(null);
        } else {
            voucherTextInputLayout.setError(gmVoucherViewModel.getMessage());
            successMessage.setVisibility(View.GONE);
        }
    }

    public boolean isVoucherOpen() {
        return checkbox.isChecked();
    }

    public String getVoucherCode() {
        return voucherEditText.getText().toString();
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)callback.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(voucherEditText.getWindowToken(), 0);
    }
}
