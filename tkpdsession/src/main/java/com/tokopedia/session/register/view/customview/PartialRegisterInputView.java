package com.tokopedia.session.register.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.presenter.RegisterInitialPresenter;

import javax.inject.Inject;

/**
 * @author by alvinatin on 11/06/18.
 */

public class PartialRegisterInputView extends BaseCustomView{

    EditText tvInputRegister;
    TextView tvMessage;
    TextView tvErrorPhone;
    TextView tvErrorEmail;
    TextView btnRegister;

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
        tvErrorPhone = view.findViewById(R.id.error_phone);
        tvErrorEmail = view.findViewById(R.id.error_email);
        btnRegister = view.findViewById(R.id.register_btn);

        renderData();
    }

    public void renderData(){
        btnRegister.setOnClickListener(new ClickRegister());
    }

    private class ClickRegister implements OnClickListener{

        @Override
        public void onClick(View v) {
            String id = tvInputRegister.getText().toString();
            presenter.validateRegister(id);
        }
    }
}
