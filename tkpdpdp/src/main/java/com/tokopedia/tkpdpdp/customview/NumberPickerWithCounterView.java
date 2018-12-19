package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.text.watcher.AfterTextWatcher;
import com.tokopedia.tkpdpdp.R;

public class NumberPickerWithCounterView extends com.tokopedia.design.component.NumberPickerWithCounterView {

    private AppCompatEditText inputQuantity;
    private String previousString;

    public NumberPickerWithCounterView(@NonNull Context context) {
        super(context);
    }

    public NumberPickerWithCounterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerWithCounterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMinValue(int minValue){
        this.minValue = minValue;
    }

    public int getMinValue(){
        return this.minValue;
    }

    public int getMaxValue(){
        return this.maxValue;
    }

    @Override
    protected void init() {
        View view = inflate(getContext(), R.layout.custom_number_picker, this);

        plusImageButton = view.findViewById(R.id.image_button_plus);
        minusImageButton = view.findViewById(R.id.image_button_minus);
        inputQuantity = view.findViewById(R.id.edittext_input_quantity);

        inputQuantity.addTextChangedListener(new AfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.equals(s.toString(), previousString)) {
                    return;
                }

                previousString = s.toString();

                int current = number;
                try {
                    current = Integer.valueOf(TextUtils.isEmpty(s.toString()) ? "0" : s.toString());
                } catch (Exception e) {
                    current = number;
                } finally {
                    setTextNumberInputView(current);
                }
                inputQuantity.setSelection(inputQuantity.getText().length());
                updateButtonState();
            }
        });
    }

    @Override
    protected void setEnabledNumberInputView(boolean enabled) {
        inputQuantity.setEnabled(enabled);
    }

    @NonNull
    @Override
    protected String getNumberTextString() {
        return inputQuantity.getText().toString();
    }

    @Override
    protected void setTextNumberInputView(int newNumber) {
        inputQuantity.setText(String.valueOf(newNumber));
    }


}
