package com.tokopedia.session.register.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.presenter.RegisterInitialPresenter;

import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * @author by alvinatin on 11/06/18.
 */

public class PartialRegisterInputView extends BaseCustomView{

    EditText tvInputRegister;
    TextView tvMessage;
    TextView tvError;
    TextView btnRegister;
    TkpdHintTextInputLayout wrapper;

    private static final Pattern pattern = Pattern.compile("^[\\d]{8,15}$");

    @Inject
    RegisterInitialPresenter presenter;

    public PartialRegisterInputView(@NonNull Context context) {
        super(context);
        init();
    }

    public PartialRegisterInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialRegisterInputView(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setPresenter(RegisterInitialPresenter presenter){
        this.presenter = presenter;
    }

    private void init(){
        View view = inflate(getContext(), R.layout.partial_register_input, this);
        tvInputRegister = (EditText) view.findViewById(R.id.input_register);
        tvMessage = view.findViewById(R.id.message);
        tvError = view.findViewById(R.id.error_message);
        btnRegister = view.findViewById(R.id.register_btn);
        wrapper = view.findViewById(R.id.input_layout);

        renderData();
    }

    public void renderData(){
        tvInputRegister.addTextChangedListener(watcher(wrapper));

        btnRegister.setOnClickListener(new ClickRegister());
    }

    public void onErrorValidate(String message){
        setWrapperError(wrapper, message);
    }

    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    private boolean isPhoneNumber(CharSequence s) {
        return pattern.matcher(s).matches();
    }


    private TextWatcher watcher(final TkpdHintTextInputLayout wrapper) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    setWrapperError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    setWrapperError(wrapper,
                            getContext().getString(R.string.register_input_empty_error));
                }

                if (isPhoneNumber(s)){
                    tvMessage.setVisibility(View.VISIBLE);
                } else {
                    tvMessage.setVisibility(View.GONE);
                }
            }
        };
    }

    public String getTextValue() {
        return tvInputRegister.getText().toString();
    }

    private class ClickRegister implements OnClickListener{

        @Override
        public void onClick(View v) {
            String id = tvInputRegister.getText().toString();
            presenter.validateRegister(id);
        }
    }
}
